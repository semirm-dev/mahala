package semir.mahovkic.mahala.data.model

data class CandidateDetails(
    val id: String,
    val name: String,
    val votingNumber: Int,
    val profileImg: String?,
    val gender: String,
    val party: Party,
    val totalVotes: Int,
    val groups: List<Group>
)
