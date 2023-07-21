package semir.mahovkic.mahala.data.model

data class CandidateDetails(
    val id: String,
    val name: String,
    val votingNumber: Int,
    val profileImg: String?,
    val party: String,
    var votes: List<CandidateVote>? = listOf()
)

data class CandidateVote(
    val voterId: String
)