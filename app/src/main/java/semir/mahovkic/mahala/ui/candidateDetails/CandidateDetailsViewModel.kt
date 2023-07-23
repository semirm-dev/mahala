package semir.mahovkic.mahala.ui.candidateDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import semir.mahovkic.mahala.data.CandidatesRepository
import semir.mahovkic.mahala.data.model.CandidateDetails
import semir.mahovkic.mahala.data.model.CandidateVote
import java.util.Locale
import javax.inject.Inject

const val VoteTag = "VOTE"

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
                Log.e(VoteTag, "loadCandidateDetails failed: ${e.response()?.message()}")
                setVoteMessage("Something went wrong!")
            }
        }
    }

    fun vote(candidateId: String, voterId: String) {
        viewModelScope.launch {
            try {
                if (!validVoter(voterId)) {
                    setVoteMessage("Invalid ID!")
                    return@launch
                }

                candidatesRepository.vote(candidateId, voterId)
                setVoteMessage("Your voting ticket has been sent successfully.")

                loadCandidateDetails(candidateId)
            } catch (e: HttpException) {
                Log.e(VoteTag, "vote failed: ${e.response()?.message()}")

                val responseMessage = e.response()?.errorBody()?.string()?.let {
                    JSONObject(it)
                }?.getString("message").toString()

                setVoteMessage(
                    responseMessage.replaceFirstChar { it.titlecase(Locale.ROOT) }
                )
            }
        }
    }

    fun setVoteMessage(message: String) {
        _voteUiState.value = VoteDetailsUiState(message)
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
    gender = gender,
    party = party,
    votes = votes?.map { v -> v.toCandidateVoteUiState() }
)


fun CandidateVote.toCandidateVoteUiState(): CandidateVoteUiState = CandidateVoteUiState(
    voterId = voterId
)