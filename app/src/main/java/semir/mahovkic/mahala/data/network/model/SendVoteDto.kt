package semir.mahovkic.mahala.data.network.model

data class SendVoteDto(
    val candidateId: String,
    val voterId: String,
)

data class VoteResponseDto(
    val message: String
)