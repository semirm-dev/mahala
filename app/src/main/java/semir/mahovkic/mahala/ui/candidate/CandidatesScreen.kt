package semir.mahovkic.mahala.ui.candidate

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import semir.mahovkic.mahala.R
import semir.mahovkic.mahala.data.CandidatesRepository
import semir.mahovkic.mahala.data.room.CandidatesDao

private const val CANDIDATES_SCREEN_TAG = "CANDIDATES_SCREEN"

@Composable
fun CandidatesScreen(
    viewModel: CandidatesViewModel = CandidatesViewModel(
        CandidatesRepository(
            CandidatesDao()
        )
    )
) {
    val uiState: CandidatesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    CandidatesList(uiState.candidatesUiState, viewModel)
}

@Composable
fun CandidatesList(candidates: List<CandidateUiState>, viewModel: CandidatesViewModel) {
    LazyColumn {
        items(candidates) { candidate ->
            CandidateCard(candidate, viewModel)
        }
    }
}

@Composable
fun CandidateCard(candidate: CandidateUiState, viewModel: CandidatesViewModel) {
    Row(modifier = Modifier
        .padding(all = 8.dp)
        .fillMaxWidth()
        .clickable {
            viewModel.vote(candidateId = candidate.id)
            viewModel.uiState.value.candidatesUiState
                .find {
                    it.id == candidate.id
                }
                ?.also {
                    Log.i(
                        CANDIDATES_SCREEN_TAG,
                        "candidate ${candidate.id} - ${candidate.votes} votes"
                    )
                }
        }) {
        Image(
            painter = painterResource(R.drawable.semirmahovkic),
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
                    text = "${candidate.name} - ${candidate.votes}",
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

@Preview
@Composable
fun CandidateCardPreview() {
    val viewModel = CandidatesViewModel(
        CandidatesRepository(
            CandidatesDao()
        )
    )

    val uiState: CandidatesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    CandidatesList(uiState.candidatesUiState, viewModel)
}