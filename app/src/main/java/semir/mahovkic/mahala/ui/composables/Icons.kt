package semir.mahovkic.mahala.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


@Composable
fun rememberAddAPhoto(color: Color = MaterialTheme.colorScheme.primary): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "add_a_photo",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18.125f, 30.667f)
                quadToRelative(3.042f, 0f, 5.063f, -2.042f)
                quadToRelative(2.02f, -2.042f, 2.02f, -5.042f)
                reflectiveQuadToRelative(-2.02f, -5.021f)
                quadToRelative(-2.021f, -2.02f, -5.063f, -2.02f)
                quadToRelative(-3f, 0f, -5f, 2.02f)
                quadToRelative(-2f, 2.021f, -2f, 5.021f)
                reflectiveQuadToRelative(2f, 5.042f)
                quadToRelative(2f, 2.042f, 5f, 2.042f)
                close()
                moveTo(4.417f, 36.583f)
                quadToRelative(-1.084f, 0f, -1.875f, -0.791f)
                quadTo(1.75f, 35f, 1.75f, 33.958f)
                verticalLineToRelative(-20.75f)
                quadToRelative(0f, -1.041f, 0.792f, -1.833f)
                quadToRelative(0.791f, -0.792f, 1.875f, -0.792f)
                horizontalLineToRelative(5.833f)
                lineToRelative(2.208f, -2.625f)
                quadToRelative(0.334f, -0.416f, 0.854f, -0.645f)
                quadToRelative(0.521f, -0.23f, 1.146f, -0.23f)
                horizontalLineToRelative(8.584f)
                quadToRelative(0.541f, 0f, 0.937f, 0.375f)
                reflectiveQuadToRelative(0.396f, 0.917f)
                verticalLineToRelative(4.833f)
                horizontalLineTo(4.417f)
                verticalLineToRelative(20.75f)
                horizontalLineToRelative(27.5f)
                verticalLineTo(18.125f)
                horizontalLineToRelative(1.333f)
                quadToRelative(0.25f, 0f, 0.479f, 0.104f)
                quadToRelative(0.229f, 0.104f, 0.417f, 0.292f)
                quadToRelative(0.187f, 0.187f, 0.292f, 0.417f)
                quadToRelative(0.104f, 0.229f, 0.104f, 0.52f)
                verticalLineToRelative(14.5f)
                quadToRelative(0f, 1.042f, -0.792f, 1.834f)
                quadToRelative(-0.792f, 0.791f, -1.833f, 0.791f)
                close()
                moveToRelative(27.5f, -26f)
                horizontalLineTo(29.75f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.395f)
                quadToRelative(-0.375f, -0.396f, -0.375f, -0.938f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.917f, -0.375f)
                horizontalLineToRelative(2.167f)
                verticalLineTo(5.75f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.916f, -0.375f)
                quadToRelative(0.584f, 0f, 0.959f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                verticalLineToRelative(2.208f)
                horizontalLineToRelative(2.166f)
                quadToRelative(0.584f, 0f, 0.959f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                quadToRelative(0f, 0.583f, -0.375f, 0.958f)
                reflectiveQuadToRelative(-0.959f, 0.375f)
                horizontalLineToRelative(-2.166f)
                verticalLineToRelative(2.167f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                reflectiveQuadToRelative(-0.959f, 0.375f)
                quadToRelative(-0.541f, 0f, -0.916f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.917f)
                close()
                moveToRelative(-27.5f, 2.625f)
                verticalLineToRelative(20.75f)
                verticalLineToRelative(-20.75f)
                close()
            }
        }.build()
    }
}