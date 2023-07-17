package semir.mahovkic.mahala.data.model

data class CandidateDetails(
    val id: String = "",
    val name: String = "",
    val profileImg: Int = 0,
    val party: String = "",
    var votes: List<CandidateVote>? = listOf()
)

data class CandidateVote(
    val voterId: String
)