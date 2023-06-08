package semir.mahovkic.mahala

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import semir.mahovkic.mahala.ui.theme.MahalaTheme

private const val LOG_TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MahalaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CandidatesList(
                        listOf(
                            Candidate(
                                1,
                                "candidate-a",
                                R.drawable.semirmahovkic,
                                "pt1"
                            )
                        )
                    )
                }
            }
        }
    }
}

data class Candidate(
    val id: Int,
    val name: String,
    val profileImg: Int,
    val party: String
)

@Composable
fun CandidateCard(candidate: Candidate) {
    Row(modifier = Modifier
        .padding(all = 8.dp)
        .fillMaxWidth()
        .clickable {
            Log.i(LOG_TAG, "clicked on candidate = [${candidate.name}]")
        }) {
        Image(
            painter = painterResource(candidate.profileImg),
            contentDescription = "Candidate profile image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 2.dp,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = candidate.name,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(all = 4.dp)
                )

            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Party - ${candidate.party}",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun CandidatesList(candidates: List<Candidate>) {
    LazyColumn {
        items(candidates) { candidate ->
            for (i in 1..50) {
                CandidateCard(candidate = candidate)
            }
        }
    }
}

@Preview
@Composable
fun CandidateCardPreview() {
    CandidatesList(
        candidates = listOf(
            Candidate(1, "candidate-a", R.drawable.semirmahovkic, "pt1")
        )
    )
}