package semir.mahovkic.mahala.ui.candidate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import semir.mahovkic.mahala.data.CandidatesRepository
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import semir.mahovkic.mahala.data.model.CandidateVote
import javax.inject.Inject

@HiltViewModel
class CandidateDetailsViewModel @Inject constructor(
    private val candidatesRepository: CandidatesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidateDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCandidateDetails("candidate-id")
    }

    private fun loadCandidateDetails(candidateId: String) {
        viewModelScope.launch {
            val candidateDetailsDeferred =
                async { candidatesRepository.getCandidateDetails(candidateId) }

            candidateDetailsDeferred.await().also {
                _uiState.value = it.toUiState()
            }
        }
    }

    fun vote(candidateId: String, voterId: String) {
        viewModelScope.launch {
            try {
                candidatesRepository.vote(candidateId, voterId)
                loadCandidateDetails(candidateId)
            } catch (e: HttpException) {
                Log.e("VOTE", "vote failed: ${e.response()?.message()}")
            }
        }
    }
}

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

fun CandidateDetails.toUiState(): CandidateDetailsUiState = CandidateDetailsUiState(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
    votes = votes.map { it.toUiState() }
)

fun CandidateVote.toUiState(): CandidateVoteUiState = CandidateVoteUiState(
    candidateId = candidateId,
    voterId = voterId
)
