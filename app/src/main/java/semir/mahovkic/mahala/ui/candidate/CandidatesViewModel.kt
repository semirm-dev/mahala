package semir.mahovkic.mahala.ui.candidate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import semir.mahovkic.mahala.data.CandidatesRepository
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
                candidatesRepository.getCandidateDetails(candidate.id).also {
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
                candidatesRepository.getCandidatesStream().collect {
                    _uiState.value =
                        CandidatesUiState(it.map { candidate -> candidate.toCandidateUiState() })
                }
            } catch (e: HttpException) {
                Log.e("VOTE", "loadCandidates failed: ${e.response()?.message()}")
            }
        }
    }
}
