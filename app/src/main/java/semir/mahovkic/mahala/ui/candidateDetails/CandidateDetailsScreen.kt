package semir.mahovkic.mahala.ui.candidateDetails

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
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
    viewModel.loadCandidateDetails(candidateId)

    val uiState: CandidateDetailsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val idScanLauncher =
        rememberLauncherForActivityResult(OneSideDocumentScan()) { oneSideScanResult: OneSideScanResult ->
            when (oneSideScanResult.resultStatus) {
                ResultStatus.FINISHED -> {
                    oneSideScanResult.result?.documentNumber?.value()?.also {
                        Log.i("SCAN", "voter $it voting for ${uiState.id}")
                        viewModel.vote(uiState.id, it)
                    }
                }

                ResultStatus.CANCELLED -> {}
                ResultStatus.EXCEPTION -> {}
                else -> {}
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
    Row(modifier = Modifier
        .padding(all = 8.dp)
        .fillMaxWidth()
        .clickable {
            onCandidateClick()
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
                    text = "${candidateDetails.name} - ${candidateDetails.votes?.size ?: 0}",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(all = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Party - ${candidateDetails.party}",
                color = Color.Blue,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}
