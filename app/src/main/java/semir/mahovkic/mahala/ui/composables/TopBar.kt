package semir.mahovkic.mahala.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import semir.mahovkic.mahala.ui.theme.TopBarLight
import semir.mahovkic.mahala.ui.theme.TopBarLighter

@Composable
fun TopBar(text: String, animated: Boolean = false) {
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            when (animated) {
                true -> {
                    InfinitelyPulsingHeart(
                        TopBarLighter,
                        TopBarLight,
                        targetSize = 1.1f,
                        scaleDuration = 1800,
                        colorDuration = 2800
                    ) { scale, color ->
                        Box(Modifier.fillMaxSize()) {
                            Text(
                                text,
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

                false -> {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            text,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(4.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}
