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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.microblink.blinkid.activity.result.OneSideScanResult
import com.microblink.blinkid.activity.result.ResultStatus
import com.microblink.blinkid.activity.result.contract.OneSideDocumentScan
import semir.mahovkic.mahala.R
import semir.mahovkic.mahala.ui.ProfileImage

@Composable
fun CandidateDetailsScreen(
    navController: NavController,
    candidateId: String,
    viewModel: CandidateDetailsViewModel
) {
    val uiState: CandidateDetailsUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val voteUiState: VoteDetailsUiState by viewModel.voteUiState.collectAsStateWithLifecycle()
    val voterId = remember {
        mutableStateOf("")
    }

    viewModel.loadCandidateDetails(candidateId)

    DisplayVoteMessage(voteUiState) {
        viewModel.resetVoteUiState()
    }

    val idScanLauncher =
        rememberLauncherForActivityResult(OneSideDocumentScan()) { scanResult: OneSideScanResult ->
            handleScanResult(scanResult) {
                scanResult.result?.let { r ->
                    r.documentNumber?.value()?.let {
                        voterId.value = it
                    }

                    r.isExpired.let { expired ->
                        if (expired) {
                            r.dateOfExpiry?.let {
                                viewModel.setVoteMessage(
                                    voterId.value,
                                    "id ${voterId.value} expired: ${it.date}"
                                )
                            }
                        }
                    }
                }
            }
        }

    CandidateDetails(candidateDetails = uiState, voterId, {
        try {
            idScanLauncher.launch()
        } catch (e: Exception) {
            Log.e("SCAN", e.toString())
        }
    }, {
        viewModel.vote(uiState.id, voterId.value)
        viewModel.setVoteMessage(voterId.value)
        voterId.value = ""
    })
}

@Composable
fun CandidateDetails(
    candidateDetails: CandidateDetailsUiState,
    voterId: MutableState<String>,
    onScanClick: () -> Unit,
    onVoteClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        DetailsView(candidateDetails)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Total votes: ${candidateDetails.votes?.size ?: 0}",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(all = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ScanView(voterId, onScanClick)

        VoteView(voterId, onVoteClick)
    }
}

@Composable
fun DetailsView(
    candidateDetails: CandidateDetailsUiState,
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        ProfileImage(candidateDetails.profileImg, 130)

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 2.dp,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = candidateDetails.name,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(all = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Nr: ${candidateDetails.votingNumber}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .align(Alignment.BottomStart)
                        )
                        Text(
                            text = candidateDetails.party,
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
}

@Composable
fun ScanView(
    voterId: MutableState<String>,
    onScanClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = voterId.value,
            onValueChange = { voterId.value = it },
            placeholder = {
                Text(
                    text = "Your ID number",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Button(
            onClick = onScanClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Scan",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentWidth()
            )
        }
    }
}

@Composable
fun VoteView(
    voterId: MutableState<String>,
    onVoteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Button(
            onClick = onVoteClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = voterId.value.trim().isNotEmpty(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Vote",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun DisplayVoteMessage(voteUiState: VoteDetailsUiState, callback: () -> Unit) {
    if (voteUiState.voterId.isNotEmpty() && voteUiState.message.isEmpty()) {
        Toast.makeText(
            LocalContext.current,
            "voter ${voteUiState.voterId} finished voting",
            Toast.LENGTH_LONG
        ).show()
    }

    if (voteUiState.message.isNotEmpty()) {
        Toast.makeText(LocalContext.current, voteUiState.message, Toast.LENGTH_LONG).show()
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