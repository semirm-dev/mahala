package semir.mahovkic.mahala.data.local.entities

class CandidateEntity(
    val id: String,
    var name: String,
    var profileImg: Int = 0,
    var party: String = "",
    var votes: Int = 0
)