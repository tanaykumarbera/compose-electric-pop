package co.tanay.electricpop.foundation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

/**
 * A bottom navigation bar following the Kinetic Pulse design system.
 *
 * This is a slot-based container — place [PopBottomBarItem] composables inside
 * the [content] lambda. Items automatically share equal width via [RowScope].
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No 1px borders on the bar — tonal surface background used instead.
 * - **Rule 2 (Tonal Shadows):** Shadow color matches surface darkened 10%, 32dp blur, 0 offset.
 * - **Rule 3 (Ghost Border):** `outlineVariant` at 15% opacity for accessibility contrast.
 * - **Rule 5 (Kinetic Interactions):** Hover scales to 1.05x, press to 0.95x, 200ms ease.
 * - **Rule 6 (Squircle Radii):** Uses `MaterialTheme.shapes.extraLarge` (85% squircle).
 * - **Rule 7 (Typography Impact):** Labels rendered uppercase via `labelSmall`.
 *
 * @param modifier Optional [Modifier] applied to the outer container.
 * @param content Slot for [PopBottomBarItem] composables.
 */
@Composable
fun PopBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val spacing = ElectricPopTheme.spacing
    val barShape = MaterialTheme.shapes.extraLarge
    val surfaceColor = MaterialTheme.colorScheme.surface
    // Design Rule 2: Tonal shadow — surface darkened 10%
    val shadowColor = surfaceColor.copy(
        red = surfaceColor.red * 0.9f,
        green = surfaceColor.green * 0.9f,
        blue = surfaceColor.blue * 0.9f,
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.md),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Design Rule 2: Tonal shadow
                .shadow(
                    elevation = 32.dp,
                    shape = barShape,
                    ambientColor = shadowColor,
                    spotColor = shadowColor,
                )
                // Design Rule 3: Ghost border for accessibility
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f),
                    shape = barShape,
                )
                .clip(barShape)
                // Design Rule 1 + Spec: Glassmorphic — surfaceBright at 70% opacity
                .background(MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.70f))
                // Top padding keeps content in the visible zone; extra bottom padding extends
                // the bar's bottom edge below the screen when placed at the bottom of the layout
                .padding(
                    start = spacing.xs,
                    end = spacing.xs,
                    top = spacing.md,
                    bottom = spacing.xl,
                )
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/**
 * An individual navigation item for use inside [PopBottomBar].
 *
 * Must be used within a [PopBottomBar] content lambda. Each item uses
 * [Modifier.weight(1f)] to share bar space equally.
 *
 * The [icon] and optional [label] slots inherit the animated content color
 * automatically via [LocalContentColor]. Use [LocalContentColor.current] in
 * [PopIcon] calls within the icon slot:
 * ```
 * icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) }
 * ```
 *
 * Labels are rendered with [MaterialTheme.typography.labelSmall] automatically —
 * just pass `Text("HOME")` as the label slot.
 *
 * Design rules enforced:
 * - **Rule 4 (Neon Glow):** Selected pill shadow uses primaryContainer at 18% opacity.
 * - **Rule 5 (Kinetic Interactions):** Hover 1.05x, press 0.95x, 200ms ease.
 * - **Rule 6 (Squircle Radii):** Selected pill uses [PopShapeFull].
 *
 * @param selected Whether this item is currently selected.
 * @param onClick Callback when the item is tapped.
 * @param icon Composable slot for the item icon. Use [LocalContentColor.current] for tint.
 * @param modifier Optional [Modifier].
 * @param label Optional composable slot for a text label beneath the icon.
 */
@Composable
fun RowScope.PopBottomBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled = true)

    // Animate pill alpha: fully opaque when selected, 0 when not
    val pillAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(200, easing = EaseInOut),
    )

    // Animate icon/label color: onPrimaryContainer when selected (on neon pill), onSurfaceVariant otherwise
    val animatedColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(200, easing = EaseInOut),
    )

    val pillContainerColor = MaterialTheme.colorScheme.primaryContainer
    val pillBackground = pillContainerColor.copy(alpha = pillAlpha)
    // Neon glow color: primaryContainer at 18% opacity for the shadow spread (Design Rule 4)
    val glowColor = pillContainerColor.copy(alpha = if (selected) 0.18f else 0f)

    Column(
        modifier = modifier
            .weight(1f)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .hoverable(interactionSource)
            .semantics {
                this.selected = selected
                role = Role.Tab
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                // Design Rule 4: Neon glow spread on selected pill
                .shadow(
                    elevation = if (selected) 12.dp else 0.dp,
                    shape = PopShapeFull,
                    ambientColor = glowColor,
                    spotColor = glowColor,
                )
                .clip(PopShapeFull)
                .background(pillBackground)
                // Selected pill gets larger padding for a capsule/stadium appearance
                .padding(
                    horizontal = if (selected) spacing.md else spacing.sm,
                    vertical = if (selected) spacing.sm else spacing.xxs,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                CompositionLocalProvider(LocalContentColor provides animatedColor) {
                    icon()
                    if (label != null) {
                        ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                            label()
                        }
                    }
                }
            }
        }
    }
}
