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
import semir.mahovkic.mahala.data.PartiesRepository
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

@HiltViewModel
class CandidatesViewModel @Inject constructor(
    private val candidatesRepository: CandidatesRepository,
    private val partiesRepository: PartiesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidatesUiState())
    val uiState = _uiState.asStateFlow()

    private val _partiesUiState = MutableStateFlow(PartiesUiState())
    val partyUiState = _partiesUiState.asStateFlow()

    init {
        loadCandidates()
        loadParties()
    }

    fun loadCandidates() {
        viewModelScope.launch {
            try {
                candidatesRepository.getCandidatesStream().collect {
                    _uiState.value =
                        CandidatesUiState(
                            false,
                            it.map { candidate -> candidate.toCandidateUiState() })
                }
            } catch (e: HttpException) {
                Log.e("VOTE", "loadCandidates failed: ${e.response()?.message()}")
            }
        }
    }

    private fun loadParties() {
        viewModelScope.launch {
            try {
                partiesRepository.getPartiesStream().collect {
                    _partiesUiState.value = PartiesUiState(it.map { party ->
                        PartyUiState(party.id, party.name)
                    })
                }
            } catch (e: HttpException) {
                Log.e("VOTE", "loadCandidates failed: ${e.response()?.message()}")
            }
        }
    }
}

fun Candidate.toCandidateUiState(): CandidateUiState = CandidateUiState(
    id = id,
    name = name,
    votingNumber = votingNumber,
    profileImg = profileImg,
    gender = gender,
    party = PartyUiState(party.id, party.name),
    groups = groups.map { GroupUiState(it.id, it.name) }
)