package semir.mahovkic.mahala.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import semir.mahovkic.mahala.data.model.Candidate

class VotesRemoteDataSource(
    private val votesApi: VotesApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getCandidatesStream(): Flow<List<Candidate>> =
        withContext(ioDispatcher) {
            votesApi.getCandidatesStream()
        }

    suspend fun incrementVote(candidateId: Int): Candidate? =
        withContext(ioDispatcher) {
            votesApi.incrementVote(candidateId)
        }
}

interface VotesApi {
    fun getCandidatesStream(): Flow<List<Candidate>>
    fun incrementVote(candidateId: Int): Candidate?
}