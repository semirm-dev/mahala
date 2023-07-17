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
    private val votesApi: VotesApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _candidates = mutableListOf<Candidate>()

    suspend fun getCandidatesStream(): Flow<List<Candidate>> =
        withContext(ioDispatcher) {
            val resp = votesApi.getCandidatesStream()
            _candidates.clear()
            _candidates.addAll(resp)
            MutableStateFlow(_candidates)
        }

    suspend fun getCandidateDetails(candidateId: String): CandidateDetails =
        withContext(ioDispatcher) {
            votesApi.getCandidateDetails(candidateId)
        }

    suspend fun vote(candidateId: String, voterId: String) =
        withContext(ioDispatcher) {
            votesApi.vote(candidateId, voterId)
        }
}

interface VotesApi {
    suspend fun getCandidatesStream(): List<Candidate>
    suspend fun getCandidateDetails(candidateId: String): CandidateDetails
    suspend fun vote(candidateId: String, voterId: String)
}