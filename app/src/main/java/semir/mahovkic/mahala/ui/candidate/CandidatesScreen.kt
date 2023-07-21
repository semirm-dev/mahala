package semir.mahovkic.mahala.ui.candidate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import semir.mahovkic.mahala.ui.ProfileImage
import semir.mahovkic.mahala.ui.Screens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CandidatesScreen(
    navController: NavController,
    viewModel: CandidatesViewModel
) {
    val uiState: CandidatesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pullRefreshState =
        rememberPullRefreshState(uiState.isRefreshing, { viewModel.loadCandidates() })

    val filterBy = remember { mutableStateOf(TextFieldValue("")) }

    Column {
        SearchView(filterBy)

        Box(Modifier.pullRefresh(pullRefreshState)) {
            CandidatesList(uiState.candidates, filterBy.value.text) { candidateId ->
                navController.navigate("${Screens.CandidateDetails.route}/${candidateId}")
            }
            PullRefreshIndicator(
                uiState.isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun CandidatesList(
    candidates: List<CandidateUiState>,
    filterBy: String,
    onCandidateClick: (candidateId: String) -> Unit
) {
    val filtered = filterCandidates(candidates, filterBy)

    LazyColumn {
        items(filtered, key = { it.id }) { candidate ->
            CandidateCard(candidate) {
                onCandidateClick(candidate.id)
            }
        }
    }
}

@Composable
fun CandidateCard(
    candidate: CandidateUiState,
    onCandidateClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .clickable {
                onCandidateClick()
            }) {
        ProfileImage(candidate.profileImg)

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
                        text = candidate.name,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(all = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Nr: ${candidate.votingNumber}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .align(Alignment.BottomStart)
                        )
                        Text(
                            text = candidate.party,
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
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

fun filterCandidates(candidates: List<CandidateUiState>, filterBy: String): List<CandidateUiState> {
    val filtered = if (filterBy.isBlank()) {
        candidates
    } else {
        candidates.filter {
            it.name.lowercase().contains(filterBy)
                    || it.party.lowercase().contains(filterBy)
                    || it.votingNumber.toString().contains(filterBy)
        }
    }

    return filtered
}