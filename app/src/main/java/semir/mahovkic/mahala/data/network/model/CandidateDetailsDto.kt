package semir.mahovkic.mahala.data.network.model

data class CandidateDetailsDto(
    val id: String,
    var name: String,
    val votingNumber: Int,
    var profileImg: String?,
    val gender: String,
    var party: String,
    var votes: List<CandidateVoteDto>? = listOf()
)

data class CandidateVoteDto(
    val voterID: String,
)