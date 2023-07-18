package semir.mahovkic.mahala.ui.candidateDetails

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.microblink.blinkid.activity.result.OneSideScanResult
import com.microblink.blinkid.activity.result.ResultStatus
import com.microblink.blinkid.activity.result.contract.OneSideDocumentScan
import semir.mahovkic.mahala.R

@Composable
fun CandidateDetailsScreen(
    navController: NavController,
    candidateId: String,
    viewModel: CandidateDetailsViewModel
) {
    val uiState: CandidateDetailsUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val voteUiState: VoteDetailsUiState by viewModel.voteUiState.collectAsStateWithLifecycle()

    viewModel.loadCandidateDetails(candidateId)

    VoteResponseMessage(voteUiState) {
        viewModel.resetVoteUiState()
    }

    val idScanLauncher =
        rememberLauncherForActivityResult(OneSideDocumentScan()) { scanResult: OneSideScanResult ->
            handleScanResult(scanResult) {
                scanResult.result?.documentNumber?.value()?.also {
                    Log.i("SCAN", "voter $it voting for ${uiState.id}")
                    viewModel.vote(uiState.id, it)
                }
            }
        }

    CandidateDetails(candidateDetails = uiState) {
        try {
            idScanLauncher.launch()
        } catch (e: Exception) {
            Log.e("SCAN", e.toString())
        }
    }
}

@Composable
fun CandidateDetails(
    candidateDetails: CandidateDetailsUiState,
    onCandidateClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        ) {
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
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 2.dp,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = "${candidateDetails.name} - total votes: ${candidateDetails.votes?.size ?: 0}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(all = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = candidateDetails.party,
                    color = Color.Blue,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Button(
                    onClick = onCandidateClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Vote", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun VoteResponseMessage(voteUiState: VoteDetailsUiState, callback: () -> Unit) {
    if (voteUiState.voterId.isNotEmpty() && voteUiState.responseMessage.isEmpty()) {
        Toast.makeText(
            LocalContext.current,
            "voter ${voteUiState.voterId} finished voting",
            Toast.LENGTH_LONG
        ).show()
    }

    if (voteUiState.responseMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, voteUiState.responseMessage, Toast.LENGTH_LONG).show()
    }

    callback()
}

fun handleScanResult(scanResult: OneSideScanResult, onFinished: () -> Unit) {
    when (scanResult.resultStatus) {
        ResultStatus.FINISHED -> {
            onFinished()
        }

        ResultStatus.CANCELLED -> {}
        ResultStatus.EXCEPTION -> {}
        else -> {}
    }
}