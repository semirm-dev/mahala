package semir.mahovkic.mahala.ui.candidate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import semir.mahovkic.mahala.ui.Screens
import semir.mahovkic.mahala.ui.composables.CandidateCard
import semir.mahovkic.mahala.ui.composables.DropdownMenuView
import semir.mahovkic.mahala.ui.composables.EmptySearchBy
import semir.mahovkic.mahala.ui.composables.SearchView


const val SearchByPlaceholder = "Search by name or number"
const val MenuSearchByPlaceholder = "Search by party"
const val EmptyFilterByParty = "All parties"


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CandidatesScreen(
    navController: NavController,
    viewModel: CandidatesViewModel
) {
    val uiState: CandidatesUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val partiesUiState: PartiesUiState by viewModel.partyUiState.collectAsStateWithLifecycle()

    val pullRefreshState =
        rememberPullRefreshState(uiState.isRefreshing, { viewModel.loadCandidates() })

    val searchBy = remember { mutableStateOf(EmptySearchBy) }
    val filterByParty = remember { mutableStateOf(EmptyFilterByParty) }

    Column {
        SearchView(searchBy, SearchByPlaceholder)

        PartiesFilter(partiesUiState, filterByParty)

        Box(Modifier.pullRefresh(pullRefreshState)) {
            CandidatesList(uiState.candidates, searchBy.value, filterByParty.value) { candidateId ->
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
    searchBy: String,
    filterByParty: String,
    onCandidateClick: (candidateId: String) -> Unit
) {
    val filtered = filterCandidates(candidates, searchBy, filterByParty)

    LazyColumn {
        items(filtered, key = { it.id }) { candidate ->
            CandidateCard(
                candidate.profileImg,
                100.dp,
                candidate.name,
                candidate.votingNumber,
                candidate.party.name,
                candidate.gender,
                20.dp
            ) {
                onCandidateClick(candidate.id)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun PartiesFilter(partiesUiState: PartiesUiState, filterByParty: MutableState<String>) {
    val parties = mutableListOf(EmptyFilterByParty)
    parties.addAll(partiesUiState.parties.map { it.name })
    DropdownMenuView(parties, filterByParty, MenuSearchByPlaceholder, true)
}

fun filterCandidates(
    candidates: List<CandidateUiState>,
    searchBy: String,
    filterByParty: String
): List<CandidateUiState> {
    val filtered = if (searchBy == EmptySearchBy && filterByParty == EmptyFilterByParty) {
        candidates
    } else {
        candidates.filter {
            (if (filterByParty == EmptyFilterByParty) true
            else it.party.name.lowercase() == filterByParty.lowercase()) &&
                    (it.name.lowercase().contains(searchBy.lowercase()) ||
                            it.votingNumber.toString().contains(searchBy))
        }
    }

    return filtered
}