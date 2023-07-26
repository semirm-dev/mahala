package semir.mahovkic.mahala.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable


@Composable
fun <T : Number> NumericSlideUpDown(
    oldValue: T,
    newValue: T,
    draw: @Composable (animatedValue: T) -> Unit
) {
    AnimatedContent(
        targetState = newValue,
        transitionSpec = {
            if (targetState.toFloat() > oldValue.toFloat()) {
                slideUp()
            } else {
                slideDown()
            }.using(SizeTransform(clip = false))
        },
        label = ""
    ) { value ->
        draw(value)
    }
}

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