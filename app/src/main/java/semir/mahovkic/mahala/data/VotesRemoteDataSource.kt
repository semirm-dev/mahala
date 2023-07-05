package semir.mahovkic.mahala.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

class VotesRemoteDataSource @Inject constructor(
    private val votesApi: VotesApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getCandidatesStream(): Flow<List<Candidate>> =
        withContext(ioDispatcher) {
            votesApi.getCandidatesStream()
        }

    suspend fun vote(candidateId: String, voterId: String) =
        withContext(ioDispatcher) {
            votesApi.vote(candidateId, voterId)
        }
}

interface VotesApi {
    suspend fun getCandidatesStream(): Flow<List<Candidate>>
    suspend fun vote(candidateId: String, voterId: String)
}