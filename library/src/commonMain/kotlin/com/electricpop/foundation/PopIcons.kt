package com.electricpop.foundation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Built-in icon set for Electric Pop.
 *
 * Provides a small set of commonly needed icons so the library
 * and its demos are self-contained. Consumers who want the full
 * Material Symbols set can add `androidx.compose.material:material-icons-core`
 * themselves — [PopIcon] accepts any [ImageVector].
 */
object PopIcons {

    val Star: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopStar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 2f)
                lineTo(15.09f, 8.26f)
                lineTo(22f, 9.27f)
                lineTo(17f, 14.14f)
                lineTo(18.18f, 21.02f)
                lineTo(12f, 17.77f)
                lineTo(5.82f, 21.02f)
                lineTo(7f, 14.14f)
                lineTo(2f, 9.27f)
                lineTo(8.91f, 8.26f)
                close()
            }
        }.build()
    }

    val Check: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopCheck",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(9f, 16.17f)
                lineTo(4.83f, 12f)
                lineTo(3.41f, 13.41f)
                lineTo(9f, 19f)
                lineTo(21f, 7f)
                lineTo(19.59f, 5.59f)
                close()
            }
        }.build()
    }

    val Close: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopClose",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(19f, 6.41f)
                lineTo(17.59f, 5f)
                lineTo(12f, 10.59f)
                lineTo(6.41f, 5f)
                lineTo(5f, 6.41f)
                lineTo(10.59f, 12f)
                lineTo(5f, 17.59f)
                lineTo(6.41f, 19f)
                lineTo(12f, 13.41f)
                lineTo(17.59f, 19f)
                lineTo(19f, 17.59f)
                lineTo(13.41f, 12f)
                close()
            }
        }.build()
    }

    val Info: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopInfo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black), pathFillType = PathFillType.EvenOdd) {
                // Circle
                moveTo(12f, 2f)
                arcTo(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = true, 12f, 22f)
                arcTo(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = true, 12f, 2f)
                close()
                // Letter i dot
                moveTo(11f, 7f)
                lineTo(13f, 7f)
                lineTo(13f, 9f)
                lineTo(11f, 9f)
                close()
                // Letter i body
                moveTo(11f, 11f)
                lineTo(13f, 11f)
                lineTo(13f, 17f)
                lineTo(11f, 17f)
                close()
            }
        }.build()
    }

    val Warning: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopWarning",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(1f, 21f)
                lineTo(12f, 2f)
                lineTo(23f, 21f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(11f, 15f)
                lineTo(13f, 15f)
                lineTo(13f, 17f)
                lineTo(11f, 17f)
                close()
                moveTo(11f, 10f)
                lineTo(13f, 10f)
                lineTo(13f, 14f)
                lineTo(11f, 14f)
                close()
            }
        }.build()
    }

    val Heart: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopHeart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 21.35f)
                lineTo(10.55f, 20.03f)
                curveTo(5.4f, 15.36f, 2f, 12.28f, 2f, 8.5f)
                curveTo(2f, 5.42f, 4.42f, 3f, 7.5f, 3f)
                curveTo(9.24f, 3f, 10.91f, 3.81f, 12f, 5.09f)
                curveTo(13.09f, 3.81f, 14.76f, 3f, 16.5f, 3f)
                curveTo(19.58f, 3f, 22f, 5.42f, 22f, 8.5f)
                curveTo(22f, 12.28f, 18.6f, 15.36f, 13.45f, 20.04f)
                close()
            }
        }.build()
    }

    val Home: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopHome",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(10f, 20f)
                lineTo(10f, 14f)
                lineTo(14f, 14f)
                lineTo(14f, 20f)
                lineTo(19f, 20f)
                lineTo(19f, 12f)
                lineTo(22f, 12f)
                lineTo(12f, 3f)
                lineTo(2f, 12f)
                lineTo(5f, 12f)
                lineTo(5f, 20f)
                close()
            }
        }.build()
    }

    val Search: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopSearch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(15.5f, 14f)
                lineTo(14.71f, 14f)
                lineTo(14.43f, 13.73f)
                arcTo(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.5f, 9.5f)
                arcTo(6.5f, 6.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 3f, 9.5f)
                arcTo(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 14.43f, 13.73f)
                lineTo(14.71f, 14f)
                lineTo(15.5f, 14f)
                lineTo(20.49f, 19f)
                lineTo(19f, 20.49f)
                close()
            }
        }.build()
    }

    val Settings: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopSettings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                // Gear — simplified as a circle with notches
                moveTo(19.14f, 12.94f)
                curveTo(19.18f, 12.64f, 19.2f, 12.33f, 19.2f, 12f)
                curveTo(19.2f, 11.68f, 19.18f, 11.36f, 19.13f, 11.06f)
                lineTo(21.16f, 9.48f)
                lineTo(19.16f, 5.52f)
                lineTo(16.87f, 6.47f)
                curveTo(16.34f, 6.06f, 15.76f, 5.72f, 15.13f, 5.47f)
                lineTo(14.8f, 3f)
                lineTo(9.2f, 3f)
                lineTo(8.87f, 5.47f)
                curveTo(8.24f, 5.72f, 7.66f, 6.07f, 7.13f, 6.47f)
                lineTo(4.84f, 5.52f)
                lineTo(2.84f, 9.48f)
                lineTo(4.87f, 11.06f)
                curveTo(4.82f, 11.36f, 4.8f, 11.69f, 4.8f, 12f)
                curveTo(4.8f, 12.31f, 4.82f, 12.64f, 4.87f, 12.94f)
                lineTo(2.84f, 14.52f)
                lineTo(4.84f, 18.48f)
                lineTo(7.13f, 17.53f)
                curveTo(7.66f, 17.94f, 8.24f, 18.28f, 8.87f, 18.53f)
                lineTo(9.2f, 21f)
                lineTo(14.8f, 21f)
                lineTo(15.13f, 18.53f)
                curveTo(15.76f, 18.28f, 16.34f, 17.93f, 16.87f, 17.53f)
                lineTo(19.16f, 18.48f)
                lineTo(21.16f, 14.52f)
                close()
                // Center circle cutout
                moveTo(12f, 15.6f)
                arcTo(3.6f, 3.6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 8.4f, 12f)
                arcTo(3.6f, 3.6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 12f, 15.6f)
                close()
            }
        }.build()
    }

    val Add: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopAdd",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(19f, 13f)
                lineTo(13f, 13f)
                lineTo(13f, 19f)
                lineTo(11f, 19f)
                lineTo(11f, 13f)
                lineTo(5f, 13f)
                lineTo(5f, 11f)
                lineTo(11f, 11f)
                lineTo(11f, 5f)
                lineTo(13f, 5f)
                lineTo(13f, 11f)
                lineTo(19f, 11f)
                close()
            }
        }.build()
    }

    val ArrowUp: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopArrowUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(7.41f, 15.41f)
                lineTo(12f, 10.83f)
                lineTo(16.59f, 15.41f)
                lineTo(18f, 14f)
                lineTo(12f, 8f)
                lineTo(6f, 14f)
                close()
            }
        }.build()
    }

    val ArrowDown: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopArrowDown",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(7.41f, 8.59f)
                lineTo(12f, 13.17f)
                lineTo(16.59f, 8.59f)
                lineTo(18f, 10f)
                lineTo(12f, 16f)
                lineTo(6f, 10f)
                close()
            }
        }.build()
    }

    val Person: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopPerson",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                // Head
                moveTo(12f, 12f)
                arcTo(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 4f)
                arcTo(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 12f)
                close()
                // Body
                moveTo(12f, 14f)
                curveTo(7.33f, 14f, 2f, 16.33f, 2f, 19f)
                lineTo(2f, 21f)
                lineTo(22f, 21f)
                lineTo(22f, 19f)
                curveTo(22f, 16.33f, 16.67f, 14f, 12f, 14f)
                close()
            }
        }.build()
    }

    val TrendUp: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopTrendUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(16f, 6f)
                lineTo(18.29f, 8.29f)
                lineTo(13.41f, 13.17f)
                lineTo(9.41f, 9.17f)
                lineTo(2f, 16.59f)
                lineTo(3.41f, 18f)
                lineTo(9.41f, 12f)
                lineTo(13.41f, 16f)
                lineTo(19.71f, 9.71f)
                lineTo(22f, 12f)
                lineTo(22f, 6f)
                close()
            }
        }.build()
    }

    val ArrowBack: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopArrowBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(20f, 11f)
                lineTo(7.83f, 11f)
                lineTo(13.42f, 5.41f)
                lineTo(12f, 4f)
                lineTo(4f, 12f)
                lineTo(12f, 20f)
                lineTo(13.41f, 18.59f)
                lineTo(7.83f, 13f)
                lineTo(20f, 13f)
                close()
            }
        }.build()
    }

    val TrendDown: ImageVector by lazy {
        ImageVector.Builder(
            name = "PopTrendDown",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(16f, 18f)
                lineTo(18.29f, 15.71f)
                lineTo(13.41f, 10.83f)
                lineTo(9.41f, 14.83f)
                lineTo(2f, 7.41f)
                lineTo(3.41f, 6f)
                lineTo(9.41f, 12f)
                lineTo(13.41f, 8f)
                lineTo(19.71f, 14.29f)
                lineTo(22f, 12f)
                lineTo(22f, 18f)
                close()
            }
        }.build()
    }
}
