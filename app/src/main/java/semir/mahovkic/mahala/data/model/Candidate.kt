package semir.mahovkic.mahala.data.model

data class Candidate(
    val id: String,
    val name: String,
    val votingNumber: Int,
    val profileImg: String?,
    val gender: String,
    val party: Party,
    val groups: List<Group>
)