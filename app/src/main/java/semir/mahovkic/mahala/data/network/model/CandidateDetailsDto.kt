package semir.mahovkic.mahala.data.network.model

data class CandidateDetailsDto(
    val id: String,
    val name: String,
    val votingNumber: Int,
    val profileImg: String?,
    val gender: String,
    val party: PartyDto,
    val totalVotes: Int,
    val groups: List<GroupDto>
)