package semir.mahovkic.mahala.data.model

data class Candidate(
    val id: String,
    var name: String,
    var profileImg: Int = 0,
    var party: String = ""
)