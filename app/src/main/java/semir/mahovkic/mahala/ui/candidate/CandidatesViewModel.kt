package semir.mahovkic.mahala.ui.candidate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
                        party = it.party
                    )
                }
            }.collect {
                _state.emit(CandidatesUiState(it))
            }
        }
    }

    fun vote(candidateId: Int) {
        Log.i("CANDIDATES_VM", "voting for candidate $candidateId")
        candidatesRepository.incrementVote(candidateId)?.run {
            Log.i("CANDIDATES_VM", "candidate votes: $votes")
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
    val party: String
)