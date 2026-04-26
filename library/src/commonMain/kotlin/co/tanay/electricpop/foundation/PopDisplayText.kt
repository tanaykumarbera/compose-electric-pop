package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

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
fun PopDisplayTextDirection.toColor(): Color {
    return when (this) {
        PopDisplayTextDirection.Positive -> MaterialTheme.colorScheme.primary
        PopDisplayTextDirection.Negative -> MaterialTheme.colorScheme.error
        PopDisplayTextDirection.Neutral -> MaterialTheme.colorScheme.onSurface
    }
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

    Row(modifier = modifier) {
        Text(
            text = mainText.uppercase(),
            style = mainStyle,
            color = resolvedColor,
            modifier = Modifier.alignByBaseline(),
        )
        if (fractionalText != null) {
            Text(
                text = fractionalText.uppercase(),
                style = fractionalStyle,
                color = resolvedColor,
                modifier = Modifier.alignByBaseline(),
            )
        }
    }
}
