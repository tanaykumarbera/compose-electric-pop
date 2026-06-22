# Electric Pop — Usage Recipes

Copy-paste starting points. Every screen is wrapped once at the root in `ElectricPopTheme`.
Signatures here are illustrative — the authoritative, always-current API (every param,
overload, and screenshot) is at <https://tanaykumarbera.github.io/compose-electric-pop/api/>.

All recipes assume:

```kotlin
import androidx.compose.foundation.layout.*
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.foundation.*
import co.tanay.electricpop.composite.*
import co.tanay.electricpop.chart.*
```

---

## 1. App shell + theme

```kotlin
@Composable
fun App() = ElectricPopTheme {            // ElectricPopTheme(darkTheme = true) for dark
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(ElectricPopTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(ElectricPopTheme.spacing.xl), // tonal gaps, not lines
    ) {
        // screen content
    }
}
```

## 2. Primary CTA + a metric card (Rule 4 glow, Rule 7 impact type)

```kotlin
PopBannerCard(
    label = "Balance",
    mainText = "$42,069",
    fractionalText = ".42",
    badgeValue = "+12.4%",
    badgeDirection = PopBadgeDirection.Up,
    style = PopBannerCardStyle.Hero,
)
Spacer(Modifier.height(ElectricPopTheme.spacing.lg))
PopButton(
    text = "Add funds",
    onClick = { /* … */ },
    style = PopButtonStyle.Primary,   // Primary CTA gets the neon glow automatically
    size = PopButtonSize.XL,
    icon = PopIcons.Add,
)
```

## 3. A form (tonal separation, no dividers)

```kotlin
var email by remember { mutableStateOf("") }
var password by remember { mutableStateOf("") }

Column(verticalArrangement = Arrangement.spacedBy(ElectricPopTheme.spacing.md)) {
    PopTextField(
        value = email,
        onValueChange = { email = it },
        label = "Email",
        leadingIcon = PopIcons.Person,
    )
    PopTextField(
        value = password,
        onValueChange = { password = it },
        label = "Password",
        visualTransformation = PasswordVisualTransformation(),
    )
    PopButton(text = "Sign in", onClick = { /* … */ }, modifier = Modifier.fillMaxWidth())
}
```

## 4. A dashboard with a chart

```kotlin
PopDashboardCard(
    title = "Revenue",
    titleValue = "$128.4k",
    items = listOf(
        PopDashboardItem(label = "Orders", value = "1,204"),
        PopDashboardItem(label = "Refunds", value = "18"),
    ),
    backgroundIcon = PopIcons.TrendUp,
)
Spacer(Modifier.height(ElectricPopTheme.spacing.xl))
PopChart(
    series = listOf(
        PopChartSeries("This week", listOf(12f, 18f, 9f, 22f, 30f, 25f, 33f)),
        PopChartSeries("Last week", listOf(10f, 14f, 11f, 16f, 20f, 19f, 24f)),
    ),
    style = PopChartStyle.Line(),     // or PopChartStyle.Bar() / PopChartStyle.Donut()
    xLabels = listOf("M", "T", "W", "T", "F", "S", "S"),
    title = "Daily revenue",
)
```

---

When in doubt: prefer an existing `Pop*` component, read tokens from `MaterialTheme` /
`ElectricPopTheme.spacing`, and check [components.md](components.md) for the full inventory.
