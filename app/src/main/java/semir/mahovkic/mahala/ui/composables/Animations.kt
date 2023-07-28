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
    // slide from bottom to top
    val enterTransition = slideInVertically { height -> height } + fadeIn()
    val exitTransition = slideOutVertically { height -> -height } + fadeOut()

    return enterTransition.togetherWith(exitTransition)
}

fun slideDown(): ContentTransform {
    // slide from top to bottom
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
    // Creates an [InfiniteTransition] instance for managing child animations.
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Creates a child animation of float type as a part of the [InfiniteTransition].
    val scale by infiniteTransition.animateFloat(
        initialValue = initialSize,
        targetValue = targetSize,
        animationSpec = infiniteRepeatable(
            // Infinitely repeating a 1000ms tween animation using default easing curve.
            animation = tween(scaleDuration),
            // After each iteration of the animation (i.e. every 1000ms), the animation will
            // start again from the [initialValue] defined above.
            // This is the default [RepeatMode]. See [RepeatMode.Reverse] below for an
            // alternative.
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Creates a Color animation as a part of the [InfiniteTransition].
    val color by infiniteTransition.animateColor(
        initialValue = initialColor,
        targetValue = targetColor, // Dark Red
        animationSpec = infiniteRepeatable(
            // Linearly interpolate between initialValue and targetValue every 1000ms.
            animation = tween(colorDuration, easing = LinearEasing),
            // Once [TargetValue] is reached, starts the next iteration in reverse (i.e. from
            // TargetValue to InitialValue). Then again from InitialValue to TargetValue. This
            // [RepeatMode] ensures that the animation value is *always continuous*.
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    composable(scale, color)
}