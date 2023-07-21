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
import semir.mahovkic.mahala.data.model.CandidateDetails
import semir.mahovkic.mahala.data.model.CandidateVote
import javax.inject.Inject

@HiltViewModel
class CandidateDetailsViewModel @Inject constructor(
    private val candidatesRepository: CandidatesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CandidateDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _voteUiState = MutableStateFlow(VoteDetailsUiState())
    val voteUiState = _voteUiState.asStateFlow()

    fun loadCandidateDetails(candidateId: String) {
        viewModelScope.launch {
            try {
                candidatesRepository.getCandidateDetails(candidateId).let {
                    _uiState.value = it.toCandidateDetailsUiState()
                }
            } catch (e: HttpException) {
                Log.e("VOTE", "loadCandidateDetails failed: ${e.response()?.message()}")
            }
        }
    }

    fun vote(candidateId: String, voterId: String) {
        viewModelScope.launch {
            try {
                if (!validVoter(voterId)) {
                    setVoteMessage(voterId, "invalid voter id")
                }

                candidatesRepository.vote(candidateId, voterId)
                loadCandidateDetails(candidateId)
            } catch (e: HttpException) {
                Log.e("VOTE", "vote failed: ${e.response()?.message()}")
                setVoteMessage(voterId, e.response()?.message().toString())
            }
        }
    }

    fun setVoteMessage(voterId: String = "", message: String = "") {
        _voteUiState.value = VoteDetailsUiState(voterId, message)
    }

    fun resetVoteUiState() {
        _voteUiState.value = VoteDetailsUiState()
    }

    private fun validVoter(voterId: String): Boolean {
        return voterId.length == 9
    }
}

fun CandidateDetails.toCandidateDetailsUiState(): CandidateDetailsUiState = CandidateDetailsUiState(
    id = id,
    name = name,
    votingNumber = votingNumber,
    profileImg = profileImg,
    party = party,
    votes = votes?.map { v -> v.toCandidateVoteUiState() }
)


fun CandidateVote.toCandidateVoteUiState(): CandidateVoteUiState = CandidateVoteUiState(
    voterId = voterId
)