package semir.mahovkic.mahala.ui.candidate

import semir.mahovkic.mahala.data.model.Candidate

data class CandidatesUiState(
    val candidates: List<CandidateUiState> = listOf(),
)

data class CandidateUiState(
    val id: String,
    val name: String,
    val profileImg: Int = 0,
    val party: String = ""
)

fun Candidate.toCandidateUiState(): CandidateUiState = CandidateUiState(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
)
