package semir.mahovkic.mahala.data.network.model

data class CandidateResponse(
    val id: Int,
    var name: String,
    var profileImg: Int,
    var party: String,
    var votes: Int
)