package semir.mahovkic.mahala.ui.candidateDetails

data class CandidateDetailsUiState(
    val id: String = "",
    val name: String = "",
    val votingNumber: Int = 0,
    val profileImg: String? = "",
    val gender: String = "",
    val party: PartyUiState = PartyUiState(),
    val totalVotes: Int = 0,
    val groupUiState: List<GroupUiState> = emptyList()
)

data class PartyUiState(
    val id: Int = 0,
    val name: String = ""
)

data class GroupUiState(
    val id: Int,
    val name: String
)

data class VoteDetailsUiState(
    val message: String = ""
)