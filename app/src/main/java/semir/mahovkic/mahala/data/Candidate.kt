package semir.mahovkic.mahala.data

data class Candidate(
    val id: Int,
    var name: String,
    var profileImg: Int,
    var party: String,
    var votes: Int
)