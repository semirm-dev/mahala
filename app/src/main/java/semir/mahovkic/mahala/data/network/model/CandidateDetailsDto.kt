package semir.mahovkic.mahala.data.network.model

data class CandidateDetailsDto(
    val id: String,
    var name: String,
    var profileImg: Int,
    var party: String,
    var votes: List<CandidateVoteDto>? = listOf()
)

data class CandidateVoteDto(
    val voterID: String,
)