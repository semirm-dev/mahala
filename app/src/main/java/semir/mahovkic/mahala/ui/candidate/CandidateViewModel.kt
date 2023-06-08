package semir.mahovkic.mahala.ui.candidate

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import semir.mahovkic.mahala.data.CandidatesRepository

class CandidateViewModel(
    candidatesRepository: CandidatesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CandidatesUiState())
    val uiState: StateFlow<CandidatesUiState>
        get() = _state
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