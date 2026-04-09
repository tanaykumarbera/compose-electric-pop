package com.electricpop.composite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.electricpop.foundation.PopBadge
import com.electricpop.foundation.PopBadgeDirection
import com.electricpop.foundation.PopDisplayText
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopDisplayTextSize
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme

/**
 * Visual style variant for [PopMetricCard].
 */
enum class PopMetricCardStyle {
    /** Neutral surface background (surfaceContainer). For secondary/supporting metrics. */
    Surface,
    /** Primary container background (primaryContainer). For the hero/primary metric. */
    Hero,
}

/**
 * A card that displays a labeled metric value with optional fractional text, directional badge,
 * and Hero or Surface styling.
 *
 * Follows the No-Line Rule: tonal surface shifts separate content areas — no dividers.
 * Ghost border is enabled for Surface style to provide accessibility separation.
 * Kinetic hover/press animation is enabled when [onClick] is non-null.
 *
 * @param label Context label shown uppercase above the main value.
 * @param mainText The primary metric value (e.g., "$42,069"). Rendered uppercase via [PopDisplayText].
 * @param modifier Optional [Modifier] for the outer container.
 * @param fractionalText Optional fractional/suffix text (e.g., ".42", "%").
 * @param badgeValue If non-null, a [PopBadge] is shown below the display value (e.g., "+12.4%").
 * @param badgeDirection Semantic direction for the badge color arrow.
 * @param displayDirection Semantic direction for the [PopDisplayText] color.
 * @param displaySize Size variant for the main display value.
 * @param style [PopMetricCardStyle.Hero] uses primaryContainer background; [PopMetricCardStyle.Surface] uses default surface tone.
 * @param onClick Optional click handler. Enables kinetic hover/press scale animation when set.
 */
@Composable
fun PopMetricCard(
    label: String,
    mainText: String,
    modifier: Modifier = Modifier,
    fractionalText: String? = null,
    badgeValue: String? = null,
    badgeDirection: PopBadgeDirection = PopBadgeDirection.Neutral,
    displayDirection: PopDisplayTextDirection = PopDisplayTextDirection.Neutral,
    displaySize: PopDisplayTextSize = PopDisplayTextSize.Large,
    style: PopMetricCardStyle = PopMetricCardStyle.Surface,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetScale = when {
        onClick == null -> 1f
        isPressed -> 0.97f
        isHovered -> 1.03f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 200),
    )

    val isHero = style == PopMetricCardStyle.Hero

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            ),
    ) {
        PopSurface(
            shape = MaterialTheme.shapes.extraLarge,
            tone = PopSurfaceTone.Default,
            containerColor = if (isHero) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified,
            ghostBorder = !isHero,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.lg),
                verticalArrangement = Arrangement.spacedBy(spacing.xs),
            ) {
                // 1. Context label
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isHero) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )

                // 2. Display value
                PopDisplayText(
                    mainText = mainText,
                    fractionalText = fractionalText,
                    direction = displayDirection,
                    size = displaySize,
                    color = if (isHero) MaterialTheme.colorScheme.onPrimaryContainer else null,
                )

                // 3. Badge (only if badgeValue != null)
                if (badgeValue != null) {
                    Spacer(Modifier.height(spacing.xxs))
                    PopBadge(value = badgeValue, direction = badgeDirection)
                }
            }
        }
    }
}
