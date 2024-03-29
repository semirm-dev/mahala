package semir.mahovkic.mahala.ui.candidates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import semir.mahovkic.mahala.ui.Screens
import semir.mahovkic.mahala.ui.composables.CandidateCard
import semir.mahovkic.mahala.ui.composables.DropDownMenuItem
import semir.mahovkic.mahala.ui.composables.DropdownMenuView
import semir.mahovkic.mahala.ui.composables.EmptyFilterByGroup
import semir.mahovkic.mahala.ui.composables.EmptyFilterByParty
import semir.mahovkic.mahala.ui.composables.EmptySearchBy
import semir.mahovkic.mahala.ui.composables.InfinitelyPulsingHeart
import semir.mahovkic.mahala.ui.composables.MenuSearchByPlaceholder
import semir.mahovkic.mahala.ui.composables.SearchByPlaceholder
import semir.mahovkic.mahala.ui.composables.SearchView
import semir.mahovkic.mahala.ui.composables.TopBar


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CandidatesScreen(
    navController: NavController,
    viewModel: CandidatesViewModel
) {
    val uiState: CandidatesUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val voteDetailsUiState: VoteDetailsUiState by viewModel.voteDetailsUiState.collectAsStateWithLifecycle()

    val pullRefreshState =
        rememberPullRefreshState(uiState.isRefreshing, {
            viewModel.loadVotingDetails()
            viewModel.loadCandidates()
        })

    val searchBy = remember { mutableStateOf(EmptySearchBy) }
    val filterByParty = remember { mutableStateOf(DropDownMenuItem(0, EmptyFilterByParty)) }
    val filterByGroup = remember { mutableStateOf(DropDownMenuItem(0, EmptyFilterByGroup)) }

    Column {
        TopBar("Choose wisely!", animated = true)

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            GroupsFilter(
                voteDetailsUiState.groups, filterByGroup, modifier = Modifier
                    .align(Alignment.CenterStart)
            )
            PartiesFilter(
                voteDetailsUiState.parties, filterByParty, modifier = Modifier
                    .align(Alignment.CenterEnd)
            )
        }

        SearchView(searchBy, SearchByPlaceholder)

        InfinitelyPulsingHeart(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.primary,
            targetSize = 2f,
            scaleDuration = 2000,
            colorDuration = 2000
        ) { _, color ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(2.dp)
                            .align(Alignment.Center)
                            .background(color = color)
                            .shadow(4.dp)
                    )
                }
            }
        }

        Box(Modifier.pullRefresh(pullRefreshState)) {
            CandidatesList(
                uiState.candidates,
                searchBy.value,
                filterByParty.value,
                filterByGroup.value
            ) { candidateId ->
                navController.navigate("${Screens.CandidateDetails.route}/${candidateId}/${filterByGroup.value.id}")
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
    filterByParty: DropDownMenuItem<Int>,
    filterByGroup: DropDownMenuItem<Int>,
    onCandidateClick: (candidateId: String) -> Unit
) {
    val filtered = filterCandidates(candidates, searchBy, filterByParty.value, filterByGroup.value)

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
    filterByParty: MutableState<DropDownMenuItem<Int>>,
    modifier: Modifier = Modifier
) {
    val partiesFilter = mutableListOf(DropDownMenuItem(0, EmptyFilterByParty))
    partiesFilter.addAll(parties.map { DropDownMenuItem(it.id, it.name) })
    DropdownMenuView(partiesFilter, filterByParty, modifier, MenuSearchByPlaceholder, true)
}

@Composable
fun GroupsFilter(
    groups: List<GroupUiState>,
    filterByGroup: MutableState<DropDownMenuItem<Int>>,
    modifier: Modifier = Modifier
) {
    val groupsFilter = mutableListOf(DropDownMenuItem(0, EmptyFilterByGroup))
    groupsFilter.addAll(groups.map { DropDownMenuItem(it.id, it.name) })
    DropdownMenuView(groupsFilter, filterByGroup, modifier)
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
