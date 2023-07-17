package semir.mahovkic.mahala.ui.candidate

data class CandidatesUiState(
    val candidates: List<CandidateUiState> = listOf(),
)

data class CandidateUiState(
    val id: String,
    val name: String,
    val profileImg: Int = 0,
    val party: String = ""
)
