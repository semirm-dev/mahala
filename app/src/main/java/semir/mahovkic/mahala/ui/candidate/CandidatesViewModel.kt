package semir.mahovkic.mahala.ui.candidate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import semir.mahovkic.mahala.data.CandidatesRepository
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

@HiltViewModel
class CandidatesViewModel @Inject constructor(
    private val candidatesRepository: CandidatesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidatesUiState())
    val uiState: StateFlow<CandidatesUiState> = _uiState.asStateFlow()

    init {
        loadCandidates()
    }

    private fun loadCandidates() {
        viewModelScope.launch {
            val candidatesDeferred = async { candidatesRepository.getCandidatesStream() }

            candidatesDeferred.await().collect {
                _uiState.value =
                    CandidatesUiState(it.map { candidate -> candidate.toUiState() }, "")
            }
        }
    }

    fun vote(candidateId: Int) {
        viewModelScope.launch {
            candidatesRepository.incrementVote(candidateId)?.let { updatedCandidate ->
                val updated = CandidatesUiState(
                    _uiState.value.candidatesUiState.also { currentState ->
                        currentState.find { currentCandidate -> currentCandidate.id == updatedCandidate.id }
                            ?.also {
                                it.votes = updatedCandidate.votes
                            }
                    },
                    "last candidate ${updatedCandidate.id} - total votes: ${updatedCandidate.votes}"
                )

                _uiState.value = updated
            }
        }
    }
}

data class CandidatesUiState(
    val candidatesUiState: List<CandidateUiState> = listOf(),
    val message: String = ""
)

data class CandidateUiState(
    val id: Int,
    val name: String,
    val profileImg: Int,
    val party: String,
    var votes: Int
)

fun Candidate.toUiState(): CandidateUiState = CandidateUiState(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
    votes = votes
)
