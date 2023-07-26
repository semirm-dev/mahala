package semir.mahovkic.mahala.ui.candidateDetails

data class CandidateDetailsUiState(
    val id: String = "",
    val name: String = "",
    val votingNumber: Int = 0,
    val profileImg: String? = "",
    val gender: String = "",
    val party: PartyUiState = PartyUiState(),
    var totalVotes: Int = 0
)

data class PartyUiState(
    val id: Int = 0,
    val name: String = ""
)

data class VoteDetailsUiState(
    val message: String = ""
)