package semir.mahovkic.mahala.ui.candidates

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import retrofit2.HttpException
import semir.mahovkic.mahala.data.CandidatesRepository
import semir.mahovkic.mahala.data.GroupsRepository
import semir.mahovkic.mahala.data.PartiesRepository
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

@HiltViewModel
class CandidatesViewModel @Inject constructor(
    private val candidatesRepository: CandidatesRepository,
    private val partiesRepository: PartiesRepository,
    private val groupsRepository: GroupsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidatesUiState())
    val uiState = _uiState.asStateFlow()

    private val _voteDetailsUiState = MutableStateFlow(VoteDetailsUiState())
    val voteDetailsUiState = _voteDetailsUiState.asStateFlow()

    init {
        loadVotingDetails()
        loadCandidates()
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

    fun loadVotingDetails() {
        viewModelScope.launch {
            try {
                val partiesResponse = async { partiesRepository.getPartiesStream() }
                val groupsResponse = async { groupsRepository.getGroupsStream() }

                combine(partiesResponse.await(), groupsResponse.await()) { p, g ->
                    VoteDetailsUiState(
                        p.map { PartyUiState(it.id, it.name) },
                        g.map { GroupUiState(it.id, it.name) })
                }.collect {
                    _voteDetailsUiState.value = it
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