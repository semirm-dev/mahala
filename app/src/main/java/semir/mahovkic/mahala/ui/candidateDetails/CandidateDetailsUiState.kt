package semir.mahovkic.mahala.ui.candidateDetails

data class CandidateDetailsUiState(
    val id: String = "",
    val name: String = "",
    val votingNumber: Int = 0,
    val profileImg: String? = "",
    val gender: String = "",
    val party: String = "",
    var votes: List<CandidateVoteUiState>? = listOf()
)

data class CandidateVoteUiState(
    val voterId: String
)

data class VoteDetailsUiState(
    val message: String = ""
)