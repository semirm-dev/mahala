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

fun CandidateDto.toCandidate(): Candidate = Candidate(
    id = id,
    name = name,
    profileImg = profileImg ?: 0,
    party = party ?: "",
)

fun CandidateVoteDto.toCandidateVote(): CandidateVote = CandidateVote(
    candidateId = candidateID,
    voterId = voterID
)