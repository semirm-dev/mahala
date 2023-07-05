package semir.mahovkic.mahala.data.network.model

data class CandidatesResponse(
    val id: String,
    var name: String,
    var profileImg: Int?,
    var party: String?,
    var votes: Int?
)