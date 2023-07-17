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
