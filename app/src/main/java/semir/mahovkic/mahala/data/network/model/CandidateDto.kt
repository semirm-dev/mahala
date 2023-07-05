package semir.mahovkic.mahala.data.network.model

data class CandidateDto(
    val id: String,
    var name: String,
    var profileImg: Int?,
    var party: String?
)

data class CandidateVoteDto(
    val candidateID: String,
    val voterID: String,
)