package semir.mahovkic.mahala.data.network.model

data class CandidateDto(
    val id: String,
    val name: String,
    val votingNumber: Int,
    val profileImg: String?,
    val gender: String,
    val party: PartyDto,
    val groups: List<GroupDto>
)