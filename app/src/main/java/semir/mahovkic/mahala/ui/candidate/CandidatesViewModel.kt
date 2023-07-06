package semir.mahovkic.mahala.ui.candidate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import semir.mahovkic.mahala.data.CandidatesRepository
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateVote
import javax.inject.Inject

@HiltViewModel
class CandidatesViewModel @Inject constructor(
    private val candidatesRepository: CandidatesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidatesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCandidates()
    }

    fun loadCandidateDetails(candidate: CandidateUiState) {
        viewModelScope.launch {
            try {
                val candidateDetailsDeferred =
                    async { candidatesRepository.getCandidateDetails(candidate.id) }

                candidateDetailsDeferred.await().also {
                    _uiState.value = CandidatesUiState(
                        _uiState.value.candidates, CandidateDetailsUiState(
                            id = candidate.id,
                            name = candidate.name,
                            profileImg = candidate.profileImg,
                            party = candidate.party,
                            votes = it.votes.map { v -> v.toCandidateVoteUiState() }
                        )
                    )
                }
            } catch (e: HttpException) {
                Log.e("VOTE", "loadCandidateDetails failed: ${e.response()?.message()}")
            }
        }
    }

    fun vote(candidateDetails: CandidateDetailsUiState, voterId: String) {
        viewModelScope.launch {
            try {
                candidatesRepository.vote(candidateDetails.id, voterId)
                loadCandidateDetails(candidateDetails.toCandidateUiState())
            } catch (e: HttpException) {
                Log.e("VOTE", "vote failed: ${e.response()?.message()}")
            }
        }
    }

    private fun loadCandidates() {
        viewModelScope.launch {
            try {
                val candidatesDeferred = async { candidatesRepository.getCandidatesStream() }

                candidatesDeferred.await().collect {
                    _uiState.value =
                        CandidatesUiState(it.map { candidate -> candidate.toCandidateUiState() })
                }
            } catch (e: HttpException) {
                Log.e("VOTE", "loadCandidates failed: ${e.response()?.message()}")
            }
        }
    }
}

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