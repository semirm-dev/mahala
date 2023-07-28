package semir.mahovkic.mahala.ui.composables

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import semir.mahovkic.mahala.ui.theme.LightPurple
import semir.mahovkic.mahala.ui.theme.Purple40
import semir.mahovkic.mahala.ui.theme.Purple80

@Composable
fun TopBar() {
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            InfinitelyPulsingHeart(
                Purple80,
                Purple40,
                targetSize = 1f,
                scaleDuration = 2000,
                colorDuration = 2000
            ) { scale, color ->
                Box(Modifier.fillMaxSize()) {
                    Text(
                        "Mahala voting",
                        style = MaterialTheme.typography.titleMedium,
                        color = color,
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.Center)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale
                            )
                    )
                }
            }
        }
    }
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
            repeatMode = RepeatMode.Restart
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