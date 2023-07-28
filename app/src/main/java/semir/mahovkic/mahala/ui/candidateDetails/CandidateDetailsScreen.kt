package semir.mahovkic.mahala.ui.candidateDetails

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.microblink.blinkid.activity.result.OneSideScanResult
import com.microblink.blinkid.activity.result.ResultStatus
import com.microblink.blinkid.activity.result.TwoSideScanResult
import com.microblink.blinkid.activity.result.contract.OneSideDocumentScan
import com.microblink.blinkid.activity.result.contract.TwoSideDocumentScan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import semir.mahovkic.mahala.ui.composables.CandidateCard
import semir.mahovkic.mahala.ui.composables.DropDownMenuItem
import semir.mahovkic.mahala.ui.composables.DropdownMenuView
import semir.mahovkic.mahala.ui.composables.EmptyFilterByGroup
import semir.mahovkic.mahala.ui.composables.TopBar
import semir.mahovkic.mahala.ui.composables.rememberAddAPhoto
import semir.mahovkic.mahala.ui.composables.slideDown
import semir.mahovkic.mahala.ui.composables.slideUp
import semir.mahovkic.mahala.ui.theme.LightPurple

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

    InfoMessage(voteUiState.message) {
        viewModel.resetVoteUiState()
    }

    val frontScanLauncher =
        rememberLauncherForActivityResult(OneSideDocumentScan()) { scanResult: OneSideScanResult ->
            handleFrontScanResult(scanResult) {
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

    val twoSideScanLauncher =
        rememberLauncherForActivityResult(TwoSideDocumentScan()) { scanResult: TwoSideScanResult ->
            handleTwoSideScanResult(scanResult) {
                scanResult.result?.let { r ->
                    r.documentNumber?.value()?.let {

                    }

                    r.personalIdNumber?.value()?.let {
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
            frontScanLauncher.launch()
//            twoSideScanLauncher.launch()
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
    Column {
        TopBar(
            "${candidateDetails.votingNumber} - ${candidateDetails.name} " +
                    if (groupItem.value.id == 0) "" else "- ${groupItem.value.value}"
        )

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
            VotesInfo(
                candidateDetails.totalVotes,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            GroupsFilter(
                candidateDetails.groupUiState,
                groupItem,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
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
                .padding(all = 8.dp)
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
    Box(
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
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            modifier = Modifier.align(Alignment.CenterStart),
            textStyle = MaterialTheme.typography.titleLarge,
        )

        Button(
            onClick = onScanClick,
            shape = CircleShape,
            contentPadding = PaddingValues(18.dp),
            border = BorderStroke(0.3.dp, MaterialTheme.colorScheme.primary),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = LightPurple),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(70.dp),
        ) {
            Icon(
                imageVector = rememberAddAPhoto(),
                contentDescription = ""
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
            modifier = Modifier
                .align(Alignment.Center)
                .size(140.dp),
            shape = CircleShape
        ) {
            Text(
                text = "Vote",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
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
fun InfoMessage(message: String, callback: () -> Unit) {
    if (message.isNotEmpty()) {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.padding(18.dp)
        ) { p ->
            p.let {
                coroutineScope.launch {
                    val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = "X"
                    )
                    when (snackBarResult) {
                        SnackbarResult.Dismissed -> callback()
                        SnackbarResult.ActionPerformed -> callback()
                    }
                }
            }
        }
    }
}

fun handleFrontScanResult(scanResult: OneSideScanResult, onFinished: () -> Unit) {
    when (scanResult.resultStatus) {
        ResultStatus.FINISHED -> {
            onFinished()
        }

        ResultStatus.CANCELLED -> {}
        ResultStatus.EXCEPTION -> {}
        else -> {}
    }
}

fun handleTwoSideScanResult(scanResult: TwoSideScanResult, onFinished: () -> Unit) {
    when (scanResult.resultStatus) {
        ResultStatus.FINISHED -> {
            onFinished()
        }

        ResultStatus.CANCELLED -> {}
        ResultStatus.EXCEPTION -> {}
        else -> {}
    }
}
