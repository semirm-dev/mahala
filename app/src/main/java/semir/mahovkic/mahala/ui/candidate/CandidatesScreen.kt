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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import semir.mahovkic.mahala.ui.ProfileImage
import semir.mahovkic.mahala.ui.Screens

const val EmptyParty = "All parties"

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

    val searchBy = remember { mutableStateOf("") }
    val filterByParty = remember { mutableStateOf(EmptyParty) }

    Column {
        SearchView(searchBy)

        PartyFilterView(partiesUiState, filterByParty)

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
            CandidateCard(candidate) {
                onCandidateClick(candidate.id)
            }
            Spacer(modifier = Modifier.height(10.dp))
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
            }
    ) {
        ProfileImage(candidate.profileImg, candidate.gender)

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

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Nr: ${candidate.votingNumber}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
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
fun PartyFilterView(partiesUiState: PartiesUiState, partyFilterBy: MutableState<String>) {
    val parties = mutableListOf(EmptyParty)
    parties.addAll(partiesUiState.parties.map { it.name })
    ExposedDropdownMenuBox(parties, partyFilterBy)
}

@Composable
fun SearchView(searchBy: MutableState<String>) {
    TextField(
        value = searchBy.value,
        onValueChange = { value ->
            searchBy.value = value
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
            if (searchBy.value != "") {
                IconButton(
                    onClick = {
                        searchBy.value = ""
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox(
    items: List<String>,
    filterBy: MutableState<String>
) {
    val expanded = remember { mutableStateOf(false) }
    val searchBy = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded.value,
                onExpandedChange = {
                    expanded.value = !expanded.value
                },
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                TextField(
                    value = filterBy.value,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .width(170.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colorScheme.primary
                    )
                )

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    SearchView(searchBy)

                    items.forEach { item ->
                        if (searchBy.value.isNotEmpty() &&
                            !item.lowercase().contains(searchBy.value.lowercase())
                        ) {
                            return@forEach
                        }

                        DropdownMenuItem(
                            onClick = {
                                filterBy.value = item
                                searchBy.value = item
                                expanded.value = false
                            },
                            content = {
                                Text(text = item)
                            }
                        )
                    }
                }
            }
        }
    }
}

fun filterCandidates(
    candidates: List<CandidateUiState>,
    searchBy: String,
    filterByParty: String
): List<CandidateUiState> {
    val filtered = if (searchBy.isEmpty() && filterByParty == EmptyParty) {
        candidates
    } else {
        candidates.filter {
            (if (filterByParty == EmptyParty) true else it.party.lowercase() == filterByParty.lowercase()) &&
                    (it.name.lowercase().contains(searchBy.lowercase()) ||
                            it.votingNumber.toString().contains(searchBy))
        }
    }

    return filtered
}