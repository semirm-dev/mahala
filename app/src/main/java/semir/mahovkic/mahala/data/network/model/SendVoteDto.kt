package semir.mahovkic.mahala.data.network.model

data class SendVoteDto(
    val voterId: String,
    val candidateId: String,
    val groupId: Int
)

data class VoteResponseDto(
    val message: String
)