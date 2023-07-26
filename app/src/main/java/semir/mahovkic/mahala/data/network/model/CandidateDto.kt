package semir.mahovkic.mahala.data.network.model

data class CandidateDto(
    val id: String,
    var name: String,
    val votingNumber: Int,
    var profileImg: String?,
    val gender: String,
    var party: PartyDto
)