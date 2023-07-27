package semir.mahovkic.mahala.ui.candidateDetails

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.microblink.blinkid.activity.result.OneSideScanResult
import com.microblink.blinkid.activity.result.ResultStatus
import com.microblink.blinkid.activity.result.contract.OneSideDocumentScan
import semir.mahovkic.mahala.ui.composables.CandidateCard
import semir.mahovkic.mahala.ui.composables.DropDownMenuItem
import semir.mahovkic.mahala.ui.composables.DropdownMenuView
import semir.mahovkic.mahala.ui.composables.EmptyFilterByGroup
import semir.mahovkic.mahala.ui.composables.slideDown
import semir.mahovkic.mahala.ui.composables.slideUp

@Composable
fun CandidateDetailsScreen(
    navController: NavController,
    candidateId: String,
    groupId: Int,
    viewModel: CandidateDetailsViewModel
) {
    val uiState: CandidateDetailsUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val voteUiState: VoteDetailsUiState by viewModel.voteUiState.collectAsStateWithLifecycle()
    val voterId = remember { mutableStateOf("") }
    val groupItem = remember { mutableStateOf(DropDownMenuItem(0, EmptyFilterByGroup)) }
    val hasVoted = remember { mutableStateOf(false) }

    if (groupId != 0 && !hasVoted.value) {
        uiState.groupUiState.find { it.id == groupId }?.let {
            groupItem.value = DropDownMenuItem(it.id, it.name)
        }
    }

    viewModel.loadCandidateDetails(candidateId)

    DisplayVoteMessage(voteUiState.message) {
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
                                    "id ${voterId.value} expired: ${it.date}"
                                )
                            }
                        }
                    }
                }
            }
        }

    CandidateDetails(uiState, voterId, groupItem, {
        try {
            idScanLauncher.launch()
        } catch (e: Exception) {
            Log.e("SCAN", e.toString())
        }
    }, {
        viewModel.vote(voterId.value, uiState.id, groupItem.value.id)
        voterId.value = ""
        hasVoted.value = true
        groupItem.value = DropDownMenuItem(0, EmptyFilterByGroup)
    })
}

@Composable
fun CandidateDetails(
    candidateDetails: CandidateDetailsUiState,
    voterId: MutableState<String>,
    groupItem: MutableState<DropDownMenuItem<Int>>,
    onScanClick: () -> Unit,
    onVoteClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        CandidateCard(
            candidateDetails.profileImg,
            130.dp,
            candidateDetails.name,
            candidateDetails.votingNumber,
            candidateDetails.party.name,
            candidateDetails.gender,
            45.dp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box {
            VotesInfo(candidateDetails.totalVotes, modifier = Modifier.align(Alignment.CenterStart))

            GroupsFilter(candidateDetails.groupUiState, groupItem, modifier = Modifier.align(Alignment.CenterEnd))
        }

        Spacer(modifier = Modifier.height(20.dp))

        ScanID(voterId, onScanClick)

        VoteButton(voterId, groupItem.value, onVoteClick)
    }
}

@Composable
fun VotesInfo(newVotes: Int, modifier: Modifier = Modifier) {
    val oldVotes = remember { mutableIntStateOf(newVotes) }

    Row(
        modifier = modifier.wrapContentWidth()
    ) {
        Text(
            text = "Total votes: ",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(all = 4.dp)
                .align(Alignment.CenterVertically)
        )

        Box {
            AnimatedContent(
                targetState = newVotes,
                transitionSpec = {
                    if (targetState > oldVotes.intValue) {
                        slideUp()
                    } else {
                        slideDown()
                    }.using(SizeTransform(clip = false))
                },
                label = ""
            ) { value ->
                Text(
                    text = if (newVotes == value) "$value" else "",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(all = 4.dp)
                )
            }
        }
    }

    if (oldVotes.intValue != newVotes) {
        oldVotes.intValue = newVotes
    }
}

@Composable
fun ScanID(
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
            singleLine = true,
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
fun VoteButton(
    voterId: MutableState<String>,
    groupItem: DropDownMenuItem<Int>,
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
            enabled = voterId.value.trim().isNotEmpty() && groupItem.id > 0,
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
fun GroupsFilter(
    groups: List<GroupUiState>,
    groupItem: MutableState<DropDownMenuItem<Int>>,
    modifier: Modifier = Modifier
) {
    val groupsFilter = mutableListOf(DropDownMenuItem(0, EmptyFilterByGroup))
    groupsFilter.addAll(groups.map { DropDownMenuItem(it.id, it.name) })
    DropdownMenuView(groupsFilter, groupItem, modifier)
}

@Composable
fun DisplayVoteMessage(message: String, callback: () -> Unit) {
    if (message.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            message,
            Toast.LENGTH_LONG
        ).show()

        callback()
    }
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
