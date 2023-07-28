package semir.mahovkic.mahala.ui.composables

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color


fun slideUp(): ContentTransform {
    val enterTransition = slideInVertically { height -> height } + fadeIn()
    val exitTransition = slideOutVertically { height -> -height } + fadeOut()

    return enterTransition.togetherWith(exitTransition)
}

fun slideDown(): ContentTransform {
    val enterTransition = slideInVertically { height -> -height } + fadeIn()
    val exitTransition = slideOutVertically { height -> height } + fadeOut()

    return enterTransition.togetherWith(exitTransition)
}


@Composable
fun InfinitelyPulsingHeart(
    initialColor: Color,
    targetColor: Color,
    initialSize: Float = 1f,
    targetSize: Float = 6f,
    scaleDuration: Int = 1000,
    colorDuration: Int = 1000,
    composable: @Composable() (scale: Float, color: Color) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale by infiniteTransition.animateFloat(
        initialValue = initialSize,
        targetValue = targetSize,
        animationSpec = infiniteRepeatable(
            animation = tween(scaleDuration),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val color by infiniteTransition.animateColor(
        initialValue = initialColor,
        targetValue = targetColor,
        animationSpec = infiniteRepeatable(
            animation = tween(colorDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    composable(scale, color)
}