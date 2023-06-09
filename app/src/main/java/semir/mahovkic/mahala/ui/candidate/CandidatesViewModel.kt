package semir.mahovkic.mahala.ui.candidate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import semir.mahovkic.mahala.data.Candidate
import semir.mahovkic.mahala.data.CandidatesRepository

class CandidatesViewModel(
    private val candidatesRepository: CandidatesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CandidatesUiState())
    val uiState: StateFlow<CandidatesUiState> = _state

    init {
        viewModelScope.launch {
            candidatesRepository.getCandidates().map { it ->
                it.map {
                    CandidateUiState(
                        id = it.id,
                        name = it.name,
                        profileImg = it.profileImg,
                        party = it.party,
                        votes = it.votes
                    )
                }
            }.collect {
                _state.emit(CandidatesUiState(it))
            }
        }
    }

    fun vote(candidateId: Int) {
        Log.i("CANDIDATES_VM", "voting for candidate $candidateId")
        candidatesRepository.incrementVote(candidateId)?.let { updatedCandidate ->
            Log.i("CANDIDATES_VM", "updated candidate votes: ${updatedCandidate.votes}")
            val updated = CandidatesUiState(_state.value.candidatesUiState.also { currentState ->
                currentState.find { currentCandidate -> currentCandidate.id == updatedCandidate.id }
                    ?.also {
                        it.votes = updatedCandidate.votes
                    }
            })

            _state.update { updated }
        }

        uiState.value.candidatesUiState.find { it.id == candidateId }?.also {
            Log.i("CANDIDATES_VM", "candidate votes: ${it.votes}")
        }
    }
}

data class CandidatesUiState(
    val candidatesUiState: List<CandidateUiState> = listOf()
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
