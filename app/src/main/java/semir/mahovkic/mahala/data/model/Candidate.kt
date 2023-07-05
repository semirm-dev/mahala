package semir.mahovkic.mahala.data.model

data class Candidate(
    val id: String,
    var name: String,
    var profileImg: Int,
    var party: String,
    var votes: Int
)