package semir.mahovkic.mahala.ui.candidate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
const val EmptyFilterByGroup = "All groups"


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CandidatesScreen(
    navController: NavController,
    viewModel: CandidatesViewModel
) {
    val uiState: CandidatesUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val voteDetailsUiState: VoteDetailsUiState by viewModel.voteDetailsUiState.collectAsStateWithLifecycle()

    val pullRefreshState =
        rememberPullRefreshState(uiState.isRefreshing, { viewModel.loadCandidates() })

    val searchBy = remember { mutableStateOf(EmptySearchBy) }
    val filterByParty = remember { mutableStateOf(EmptyFilterByParty) }
    val filterByGroup = remember { mutableStateOf(EmptyFilterByGroup) }

    Column {
        SearchView(searchBy, SearchByPlaceholder)

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            GroupsFilter(
                voteDetailsUiState.groups, filterByGroup, modifier = Modifier
                    .align(Alignment.BottomStart)
            )
            PartiesFilter(
                voteDetailsUiState.parties, filterByParty, modifier = Modifier
                    .align(Alignment.BottomEnd)
            )
        }

        Box(Modifier.pullRefresh(pullRefreshState)) {
            CandidatesList(
                uiState.candidates,
                searchBy.value,
                filterByParty.value,
                filterByGroup.value
            ) { candidateId ->
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
    filterByGroup: String,
    onCandidateClick: (candidateId: String) -> Unit
) {
    val filtered = filterCandidates(candidates, searchBy, filterByParty, filterByGroup)

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
fun PartiesFilter(
    parties: List<PartyUiState>,
    filterByParty: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val partiesFilter = mutableListOf(EmptyFilterByParty)
    partiesFilter.addAll(parties.map { it.name })
    DropdownMenuView(partiesFilter, filterByParty, modifier, MenuSearchByPlaceholder, true)
}

@Composable
fun GroupsFilter(
    groups: List<GroupUiState>,
    filterByParty: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val groupsFilter = mutableListOf(EmptyFilterByGroup)
    groupsFilter.addAll(groups.map { it.name })
    DropdownMenuView(groupsFilter, filterByParty, modifier)
}

fun filterCandidates(
    candidates: List<CandidateUiState>,
    searchBy: String,
    filterByParty: String,
    filterByGroup: String
): List<CandidateUiState> {
    val filtered =
        if (searchBy == EmptySearchBy &&
            filterByParty == EmptyFilterByParty &&
            filterByGroup == EmptyFilterByGroup
        ) {
            candidates
        } else {
            candidates.filter {
                (it.name.lowercase().contains(searchBy.lowercase()) ||
                        it.votingNumber.toString().contains(searchBy))

                        &&

                        (if (filterByParty == EmptyFilterByParty) true
                        else it.party.name.lowercase() == filterByParty.lowercase())

                        &&

                        (if (filterByGroup == EmptyFilterByGroup) true
                        else it.groups.map { g -> g.name }.contains(filterByGroup))
            }
        }

    return filtered
}