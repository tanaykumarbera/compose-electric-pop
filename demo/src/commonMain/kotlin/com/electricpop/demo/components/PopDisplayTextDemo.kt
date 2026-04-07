package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.electricpop.foundation.PopDisplayText
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopDisplayTextSize
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopDisplayTextDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Size Variants
        Text(
            text = "SIZE VARIANTS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDisplayText(
            mainText = "$42,069",
            fractionalText = ".42",
            size = PopDisplayTextSize.Large,
        )
        PopDisplayText(
            mainText = "$42,069",
            fractionalText = ".42",
            size = PopDisplayTextSize.Medium,
        )
        PopDisplayText(
            mainText = "$42,069",
            fractionalText = ".42",
            size = PopDisplayTextSize.Small,
        )

        // Section: Directional Coloring
        Text(
            text = "DIRECTIONAL COLORING",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDisplayText(
            mainText = "+$8,421",
            fractionalText = ".00",
            direction = PopDisplayTextDirection.Positive,
            size = PopDisplayTextSize.Medium,
        )
        PopDisplayText(
            mainText = "-$3,200",
            fractionalText = ".00",
            direction = PopDisplayTextDirection.Negative,
            size = PopDisplayTextSize.Medium,
        )
        PopDisplayText(
            mainText = "$12,847",
            fractionalText = ".50",
            direction = PopDisplayTextDirection.Neutral,
            size = PopDisplayTextSize.Medium,
        )

        // Section: With and Without Fractional
        Text(
            text = "WITH AND WITHOUT FRACTIONAL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDisplayText(
            mainText = "$1,024",
            fractionalText = ".99",
            direction = PopDisplayTextDirection.Positive,
            size = PopDisplayTextSize.Small,
        )
        PopDisplayText(
            mainText = "$1,024",
            direction = PopDisplayTextDirection.Positive,
            size = PopDisplayTextSize.Small,
        )

        // Section: Color Override
        Text(
            text = "COLOR OVERRIDE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDisplayText(
            mainText = "$9,999",
            fractionalText = ".99",
            color = MaterialTheme.colorScheme.tertiary,
            size = PopDisplayTextSize.Medium,
        )

        // Section: Real-World Examples
        Text(
            text = "REAL-WORLD EXAMPLES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        // Hero metric
        PopDisplayText(
            mainText = "$128,400",
            fractionalText = ".00",
            direction = PopDisplayTextDirection.Positive,
            size = PopDisplayTextSize.Large,
        )
        // Percentage
        PopDisplayText(
            mainText = "+24",
            fractionalText = "%",
            direction = PopDisplayTextDirection.Positive,
            size = PopDisplayTextSize.Medium,
        )
        // Loss
        PopDisplayText(
            mainText = "-$3,850",
            fractionalText = ".25",
            direction = PopDisplayTextDirection.Negative,
            size = PopDisplayTextSize.Medium,
        )
        // Compact
        PopDisplayText(
            mainText = "42k",
            direction = PopDisplayTextDirection.Neutral,
            size = PopDisplayTextSize.Small,
        )
    }
}
