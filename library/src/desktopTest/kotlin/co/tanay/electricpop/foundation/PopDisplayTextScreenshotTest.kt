package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopDisplayTextScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popDisplayText_allVariants_light() = runDesktopComposeUiTest(width = 600, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Large size
                    Text(
                        text = "LARGE SIZE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopDisplayText(
                        mainText = "$42,069",
                        fractionalText = ".42",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Large,
                    )
                    PopDisplayText(
                        mainText = "-$1,200",
                        fractionalText = ".00",
                        direction = PopDisplayTextDirection.Negative,
                        size = PopDisplayTextSize.Large,
                    )
                    PopDisplayText(
                        mainText = "$12,847",
                        fractionalText = ".50",
                        direction = PopDisplayTextDirection.Neutral,
                        size = PopDisplayTextSize.Large,
                    )

                    // Medium size
                    Text(
                        text = "MEDIUM SIZE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopDisplayText(
                        mainText = "+2.4",
                        fractionalText = "%",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Medium,
                    )
                    PopDisplayText(
                        mainText = "-15.3",
                        fractionalText = "%",
                        direction = PopDisplayTextDirection.Negative,
                        size = PopDisplayTextSize.Medium,
                    )
                    PopDisplayText(
                        mainText = "3,500",
                        fractionalText = ".00",
                        direction = PopDisplayTextDirection.Neutral,
                        size = PopDisplayTextSize.Medium,
                    )

                    // Small size
                    Text(
                        text = "SMALL SIZE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopDisplayText(
                        mainText = "$890",
                        fractionalText = ".25",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Small,
                    )
                    PopDisplayText(
                        mainText = "-$45",
                        fractionalText = ".00",
                        direction = PopDisplayTextDirection.Negative,
                        size = PopDisplayTextSize.Small,
                    )
                    PopDisplayText(
                        mainText = "1,024",
                        direction = PopDisplayTextDirection.Neutral,
                        size = PopDisplayTextSize.Small,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDisplayText_allVariants_light.png"
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popDisplayText_allVariants_dark() = runDesktopComposeUiTest(width = 600, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Large size
                    Text(
                        text = "LARGE SIZE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopDisplayText(
                        mainText = "$42,069",
                        fractionalText = ".42",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Large,
                    )
                    PopDisplayText(
                        mainText = "-$1,200",
                        fractionalText = ".00",
                        direction = PopDisplayTextDirection.Negative,
                        size = PopDisplayTextSize.Large,
                    )
                    PopDisplayText(
                        mainText = "$12,847",
                        fractionalText = ".50",
                        direction = PopDisplayTextDirection.Neutral,
                        size = PopDisplayTextSize.Large,
                    )

                    // Medium size
                    Text(
                        text = "MEDIUM SIZE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopDisplayText(
                        mainText = "+2.4",
                        fractionalText = "%",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Medium,
                    )
                    PopDisplayText(
                        mainText = "-15.3",
                        fractionalText = "%",
                        direction = PopDisplayTextDirection.Negative,
                        size = PopDisplayTextSize.Medium,
                    )
                    PopDisplayText(
                        mainText = "3,500",
                        fractionalText = ".00",
                        direction = PopDisplayTextDirection.Neutral,
                        size = PopDisplayTextSize.Medium,
                    )

                    // Small size
                    Text(
                        text = "SMALL SIZE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopDisplayText(
                        mainText = "$890",
                        fractionalText = ".25",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Small,
                    )
                    PopDisplayText(
                        mainText = "-$45",
                        fractionalText = ".00",
                        direction = PopDisplayTextDirection.Negative,
                        size = PopDisplayTextSize.Small,
                    )
                    PopDisplayText(
                        mainText = "1,024",
                        direction = PopDisplayTextDirection.Neutral,
                        size = PopDisplayTextSize.Small,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDisplayText_allVariants_dark.png"
        )
    }
}
