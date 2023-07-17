package semir.mahovkic.mahala.ui.candidateDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import semir.mahovkic.mahala.data.CandidatesRepository
import semir.mahovkic.mahala.data.model.CandidateVote
import javax.inject.Inject

@HiltViewModel
class CandidateDetailsViewModel @Inject constructor(
    private val candidatesRepository: CandidatesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidateDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun loadCandidateDetails(candidateId: String) {
        viewModelScope.launch {
            try {
                candidatesRepository.getCandidateDetails(candidateId).also {
                    _uiState.value = CandidateDetailsUiState(
                        id = it.id,
                        name = it.name,
                        profileImg = it.profileImg,
                        party = it.party,
                        votes = it.votes?.map { v -> v.toCandidateVoteUiState() }
                    )
                }
            } catch (e: HttpException) {
                Log.e("VOTE", "loadCandidateDetails failed: ${e.response()?.message()}")
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

fun CandidateVote.toCandidateVoteUiState(): CandidateVoteUiState = CandidateVoteUiState(
    voterId = voterId
)