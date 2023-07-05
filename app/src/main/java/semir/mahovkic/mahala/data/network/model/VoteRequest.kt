package semir.mahovkic.mahala.data.network.model

data class SendVoteRequest(
    val candidateId: String,
    val voterId: String,
)

data class SendVoteResponse(val message: String)