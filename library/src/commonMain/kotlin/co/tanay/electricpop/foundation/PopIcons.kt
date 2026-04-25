package co.tanay.electricpop.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import compose_electric_pop.library.generated.resources.Res
import compose_electric_pop.library.generated.resources.pop_ic_add
import compose_electric_pop.library.generated.resources.pop_ic_arrow_back
import compose_electric_pop.library.generated.resources.pop_ic_arrow_down
import compose_electric_pop.library.generated.resources.pop_ic_arrow_forward
import compose_electric_pop.library.generated.resources.pop_ic_arrow_up
import compose_electric_pop.library.generated.resources.pop_ic_bolt
import compose_electric_pop.library.generated.resources.pop_ic_check
import compose_electric_pop.library.generated.resources.pop_ic_check_circle
import compose_electric_pop.library.generated.resources.pop_ic_close
import compose_electric_pop.library.generated.resources.pop_ic_favorite
import compose_electric_pop.library.generated.resources.pop_ic_home
import compose_electric_pop.library.generated.resources.pop_ic_info
import compose_electric_pop.library.generated.resources.pop_ic_layers
import compose_electric_pop.library.generated.resources.pop_ic_menu
import compose_electric_pop.library.generated.resources.pop_ic_person
import compose_electric_pop.library.generated.resources.pop_ic_puzzle
import compose_electric_pop.library.generated.resources.pop_ic_search
import compose_electric_pop.library.generated.resources.pop_ic_settings
import compose_electric_pop.library.generated.resources.pop_ic_sparkle
import compose_electric_pop.library.generated.resources.pop_ic_star
import compose_electric_pop.library.generated.resources.pop_ic_tokens
import compose_electric_pop.library.generated.resources.pop_ic_trending_down
import compose_electric_pop.library.generated.resources.pop_ic_trending_up
import compose_electric_pop.library.generated.resources.pop_ic_warning
import org.jetbrains.compose.resources.vectorResource

/**
 * Built-in icon set for Electric Pop.
 *
 * Icons are Material Symbols Outlined (24dp) loaded from vector drawable
 * resources. The library is self-contained — consumers who want the full
 * Material Symbols set can add `androidx.compose.material:material-icons-core`
 * themselves; [PopIcon] accepts any [ImageVector].
 */
object PopIcons {

    val Star: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_star)
    val Check: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_check)
    val Close: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_close)
    val Info: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_info)
    val Warning: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_warning)
    val Heart: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_favorite)
    val Home: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_home)
    val Search: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_search)
    val Settings: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_settings)
    val Add: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_add)
    val ArrowUp: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_arrow_up)
    val ArrowDown: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_arrow_down)
    val ArrowBack: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_arrow_back)
    val ArrowForward: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_arrow_forward)
    val Person: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_person)
    val TrendUp: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_trending_up)
    val TrendDown: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_trending_down)
    val Bolt: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_bolt)
    val Sparkle: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_sparkle)
    val CheckCircle: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_check_circle)
    val Layers: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_layers)
    val Puzzle: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_puzzle)
    val Tokens: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_tokens)
    val Menu: ImageVector @Composable get() = vectorResource(Res.drawable.pop_ic_menu)
}
