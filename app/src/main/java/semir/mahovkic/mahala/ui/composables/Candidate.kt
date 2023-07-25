package semir.mahovkic.mahala.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CandidateInfo(name: String, votingNumber: Int, party: String, height: Dp, modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(all = 4.dp)
                )

                Spacer(modifier = Modifier.height(height))

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Nr: $votingNumber",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .align(Alignment.BottomStart)
                    )
                    Text(
                        text = party,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .align(Alignment.BottomEnd),
                    )
                }
            }
        }
    }
}