package semir.mahovkic.mahala.ui.candidateDetails

import semir.mahovkic.mahala.data.model.CandidateVote

data class CandidateDetailsUiState(
    val id: String = "",
    val name: String = "",
    val profileImg: Int = 0,
    val party: String = "",
    var votes: List<CandidateVoteUiState>? = listOf()
)

data class CandidateVoteUiState(
    val voterId: String
)

fun CandidateVote.toCandidateVoteUiState(): CandidateVoteUiState = CandidateVoteUiState(
    voterId = voterId
)