package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.isSpecified

/**
 * Direction for [PopDisplayText], determining its semantic text color.
 */
enum class PopDisplayTextDirection {
    /** Positive/green — uses primary color */
    Positive,

    /** Negative/red — uses error color */
    Negative,

    /** No direction — uses onSurface (default text color) */
    Neutral,
}

/**
 * Resolves a [PopDisplayTextDirection] to a theme-based color.
 */
@Composable
fun PopDisplayTextDirection.toColor(): Color = when (this) {
    PopDisplayTextDirection.Positive -> MaterialTheme.colorScheme.primary
    PopDisplayTextDirection.Negative -> MaterialTheme.colorScheme.error
    PopDisplayTextDirection.Neutral -> MaterialTheme.colorScheme.onSurface
}

/**
 * Size for [PopDisplayText], mapping to Material typography styles.
 */
enum class PopDisplayTextSize {
    /** displayLarge (57sp main) — for primary hero metrics */
    Large,

    /** displayMedium (45sp main) — for secondary metrics */
    Medium,

    /** displaySmall (36sp main) — for tertiary/inline metrics */
    Small,
}

/**
 * Large emphasized text display with optional fractional part, directional coloring,
 * and split main/secondary sizing using Space Grotesk Black Italic.
 *
 * Both [mainText] and [fractionalText] are rendered uppercase per Typography Impact rule.
 * Texts are aligned by baseline so the smaller fractional part sits flush with the main value.
 *
 * @param mainText The primary value to display (e.g., "$42,069"). Rendered uppercase.
 * @param modifier Optional [Modifier] for the container row.
 * @param fractionalText Optional fractional/suffix text (e.g., ".42", "%"). Rendered uppercase.
 * @param direction Semantic direction that determines the text color. Ignored if [color] is non-null.
 * @param size Size variant controlling the typography scale.
 * @param color Explicit color override. If non-null, overrides [direction] coloring.
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopDisplayText_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopDisplayText_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
@Composable
fun PopDisplayText(
    mainText: String,
    modifier: Modifier = Modifier,
    fractionalText: String? = null,
    direction: PopDisplayTextDirection = PopDisplayTextDirection.Neutral,
    size: PopDisplayTextSize = PopDisplayTextSize.Large,
    color: Color? = null,
) {
    val resolvedColor = color ?: direction.toColor()

    val mainStyle: TextStyle
    val fractionalStyle: TextStyle
    when (size) {
        PopDisplayTextSize.Large -> {
            mainStyle = MaterialTheme.typography.displayLarge
            fractionalStyle = MaterialTheme.typography.headlineLarge
        }
        PopDisplayTextSize.Medium -> {
            mainStyle = MaterialTheme.typography.displayMedium
            fractionalStyle = MaterialTheme.typography.headlineSmall
        }
        PopDisplayTextSize.Small -> {
            mainStyle = MaterialTheme.typography.displaySmall
            fractionalStyle = MaterialTheme.typography.titleLarge
        }
    }

    val mainUpper = mainText.uppercase()
    val fractionalUpper = fractionalText?.uppercase()

    BoxWithConstraints(modifier = modifier) {
        // Auto-shrink: measure both texts at their base styles and compute a single
        // scale factor so main + fractional stay visually proportional. Floor at
        // MIN_AUTO_SCALE to avoid illegibility for absurdly long strings.
        val measurer = rememberTextMeasurer()
        val mainMeasured = measurer.measure(text = mainUpper, style = mainStyle)
        val fractionalMeasured = fractionalUpper?.let {
            measurer.measure(text = it, style = fractionalStyle)
        }
        val naturalWidth = mainMeasured.size.width + (fractionalMeasured?.size?.width ?: 0)
        val scale = if (constraints.hasBoundedWidth && naturalWidth > constraints.maxWidth) {
            (constraints.maxWidth.toFloat() / naturalWidth.toFloat())
                .coerceAtLeast(MIN_AUTO_SCALE)
        } else {
            1f
        }

        val effectiveMainStyle = if (scale < 1f) mainStyle.scaleTypography(scale) else mainStyle
        val effectiveFractionalStyle = if (scale < 1f) fractionalStyle.scaleTypography(scale) else fractionalStyle

        Row {
            Text(
                text = mainUpper,
                style = effectiveMainStyle,
                color = resolvedColor,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.alignByBaseline(),
            )
            if (fractionalUpper != null) {
                Text(
                    text = fractionalUpper,
                    style = effectiveFractionalStyle,
                    color = resolvedColor,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.alignByBaseline(),
                )
            }
        }
    }
}

private const val MIN_AUTO_SCALE = 0.5f

private fun TextStyle.scaleTypography(scale: Float): TextStyle = copy(
    fontSize = if (fontSize.isSpecified) fontSize * scale else fontSize,
    lineHeight = if (lineHeight.isSpecified) lineHeight * scale else lineHeight,
)
