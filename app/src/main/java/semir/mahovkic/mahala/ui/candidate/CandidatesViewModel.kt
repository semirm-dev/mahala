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

    fun vote(candidateId: String, voterId: String) {
        viewModelScope.launch {
            try {
                candidatesRepository.vote(candidateId, voterId)
                loadCandidates()
            }
            catch (e: HttpException) {
                Log.e("VOTE", "vote failed: ${e.response()?.message()}")
            }
        }
    }
}

data class CandidatesUiState(
    val candidatesUiState: List<CandidateUiState> = listOf(),
    val message: String = ""
)

data class CandidateUiState(
    val id: String,
    val name: String,
    val profileImg: Int = 0,
    val party: String = "",
    var votes: Int = 0
)

fun Candidate.toUiState(): CandidateUiState = CandidateUiState(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
    votes = votes
)
