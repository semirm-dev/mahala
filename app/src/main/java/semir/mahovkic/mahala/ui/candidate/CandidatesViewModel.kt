package semir.mahovkic.mahala.ui.candidate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val uiState: StateFlow<CandidatesUiState> = _uiState

    private val _detailsUiState = MutableStateFlow((CandidateDetailsUiState()))
    val detailsUiState: StateFlow<CandidateDetailsUiState> = _detailsUiState

    init {
        loadCandidates()
    }

    private fun loadCandidates() {
        viewModelScope.launch {
            val candidatesDeferred = async { candidatesRepository.getCandidatesStream() }

            candidatesDeferred.await().collect {
                _uiState.value =
                    CandidatesUiState(it.map { candidate -> candidate.toUiState() })
            }
        }
    }

    fun vote(candidateId: String, voterId: String) {
        viewModelScope.launch {
            try {
                candidatesRepository.vote(candidateId, voterId)
                candidatesRepository.getCandidateDetails(candidateId)?.also {
                    _detailsUiState.value =
                        CandidateDetailsUiState(it.votes.map { v -> v.toUiState() })
                }

            } catch (e: HttpException) {
                Log.e("VOTE", "vote failed: ${e.response()?.message()}")
            }
        }
    }
}

data class CandidatesUiState(
    val candidatesUiState: List<CandidateUiState> = listOf()
)

data class CandidateUiState(
    val id: String,
    val name: String,
    val profileImg: Int = 0,
    val party: String = "",
)

fun Candidate.toUiState(): CandidateUiState = CandidateUiState(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
)

data class CandidateDetailsUiState(
    var votes: List<CandidateVoteUiState> = listOf()
)

data class CandidateVoteUiState(
    val candidateId: String,
    val voterId: String
)

fun CandidateVote.toUiState(): CandidateVoteUiState = CandidateVoteUiState(
    candidateId = candidateId,
    voterId = voterId
)