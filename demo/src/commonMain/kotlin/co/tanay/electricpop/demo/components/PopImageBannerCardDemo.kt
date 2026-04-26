package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.composite.PopImageBannerCard
import co.tanay.electricpop.composite.PopImageBannerTextAnchor
import co.tanay.electricpop.theme.ElectricPopTheme
import compose_electric_pop.library.generated.resources.Res
import compose_electric_pop.library.generated.resources.pop_banner_hero
import org.jetbrains.compose.resources.painterResource

@Composable
fun PopImageBannerCardDemo() {
    val spacing = ElectricPopTheme.spacing
    val hero = painterResource(Res.drawable.pop_banner_hero)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        SectionLabel("BOTTOM START (DEFAULT)")
        PopImageBannerCard(
            painter = hero,
            eyebrow = "Electric Pop",
            headline = "Encrypted\nWealth\nSimplified",
            subtitle = "A single surface for everything you own.",
            textAnchor = PopImageBannerTextAnchor.BottomStart,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("TOP END")
        PopImageBannerCard(
            painter = hero,
            eyebrow = "Limited",
            headline = "Drop 04",
            textAnchor = PopImageBannerTextAnchor.TopEnd,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("CENTER, RADIAL SCRIM")
        PopImageBannerCard(
            painter = hero,
            headline = "Encrypted\nWealth\nSimplified",
            textAnchor = PopImageBannerTextAnchor.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("BOTTOM CENTER, NO SCRIM")
        PopImageBannerCard(
            painter = hero,
            headline = "Pop Off",
            subtitle = "For the people who refuse beige.",
            textAnchor = PopImageBannerTextAnchor.BottomCenter,
            scrim = false,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("HEIGHT-CONSTRAINED (120DP)")
        PopImageBannerCard(
            painter = hero,
            eyebrow = "Thin banner",
            headline = "Stay Charged",
            textAnchor = PopImageBannerTextAnchor.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        )

        SectionLabel("CLICKABLE (KINETIC)")
        PopImageBannerCard(
            painter = hero,
            eyebrow = "Try me",
            headline = "Tap To Feel It",
            subtitle = "Hover scale + press scale with 200ms ease.",
            textAnchor = PopImageBannerTextAnchor.BottomStart,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
