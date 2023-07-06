package semir.mahovkic.mahala.ui.candidate

import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateVote

data class CandidatesUiState(
    val candidates: List<CandidateUiState> = listOf(),
    val candidateDetails: CandidateDetailsUiState = CandidateDetailsUiState()
)

data class CandidateUiState(
    val id: String,
    val name: String,
    val profileImg: Int = 0,
    val party: String = ""
)

data class CandidateDetailsUiState(
    val id: String = "",
    val name: String = "",
    val profileImg: Int = 0,
    val party: String = "",
    var votes: List<CandidateVoteUiState> = listOf()
)

data class CandidateVoteUiState(
    val candidateId: String,
    val voterId: String
)

fun Candidate.toCandidateUiState(): CandidateUiState = CandidateUiState(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
)

fun CandidateDetailsUiState.toCandidateUiState(): CandidateUiState = CandidateUiState(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
)

fun CandidateVote.toCandidateVoteUiState(): CandidateVoteUiState = CandidateVoteUiState(
    candidateId = candidateId,
    voterId = voterId
)