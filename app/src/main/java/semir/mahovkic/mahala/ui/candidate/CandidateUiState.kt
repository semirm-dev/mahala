package semir.mahovkic.mahala.ui.candidate

data class CandidatesUiState(
    val isRefreshing: Boolean = false,
    val candidates: List<CandidateUiState> = emptyList()
)

data class CandidateUiState(
    val id: String,
    val name: String,
    val votingNumber: Int,
    val profileImg: String?,
    val gender: String,
    val party: PartyUiState,
    val groups: List<GroupUiState> = emptyList()
)

data class VoteDetailsUiState(
    val parties: List<PartyUiState> = emptyList(),
    val groups: List<GroupUiState> = emptyList()
)

data class PartyUiState(
    val id: Int,
    val name: String
)

data class GroupUiState(
    val id: Int,
    val name: String
)