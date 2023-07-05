package semir.mahovkic.mahala.data.model

data class CandidateDetails(
    var votes: List<CandidateVote> = listOf()

)

data class CandidateVote(
    val candidateId: String,
    val voterId: String
)