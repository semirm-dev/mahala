package semir.mahovkic.mahala.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import javax.inject.Inject

class VotesRemoteDataSource @Inject constructor(
    private val api: VotingApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _candidates = mutableListOf<Candidate>()

    suspend fun getCandidatesStream(): Flow<List<Candidate>> =
        withContext(ioDispatcher) {
            val resp = api.getCandidates()
            _candidates.clear()
            _candidates.addAll(resp)
            MutableStateFlow(_candidates)
        }

    suspend fun getCandidateDetails(candidateId: String): CandidateDetails =
        withContext(ioDispatcher) {
            api.getCandidateDetails(candidateId)
        }

    suspend fun vote(voterId: String, candidateId: String, groupId: Int) =
        withContext(ioDispatcher) {
            api.vote(voterId, candidateId, groupId)
        }
}
