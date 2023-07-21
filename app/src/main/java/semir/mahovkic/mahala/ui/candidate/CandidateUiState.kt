package semir.mahovkic.mahala.ui.candidate

data class CandidatesUiState(
    val isRefreshing: Boolean = false,
    val candidates: List<CandidateUiState> = listOf(),
)

data class CandidateUiState(
    val id: String,
    val name: String,
    val votingNumber: Int,
    val profileImg: String?,
    val gender: String,
    val party: String
)
