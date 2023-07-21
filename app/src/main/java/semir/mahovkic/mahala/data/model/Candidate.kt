package semir.mahovkic.mahala.data.model

data class Candidate(
    val id: String,
    var name: String,
    val votingNumber: Int,
    var profileImg: String?,
    var party: String
)