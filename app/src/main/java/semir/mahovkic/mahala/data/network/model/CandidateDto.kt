package semir.mahovkic.mahala.data.network.model

import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateVote

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