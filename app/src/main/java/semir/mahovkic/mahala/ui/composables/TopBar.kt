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
