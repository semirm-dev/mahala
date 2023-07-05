package semir.mahovkic.mahala.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

class VotesRemoteDataSource @Inject constructor(
    private val votesApi: VotesApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getCandidatesStream(): Flow<List<Candidate>> =
        withContext(ioDispatcher) {
            votesApi.getCandidatesStream()
        }

    suspend fun incrementVote(candidateId: String): Candidate? =
        withContext(ioDispatcher) {
            votesApi.incrementVote(candidateId)
        }
}

interface VotesApi {
    suspend fun getCandidatesStream(): Flow<List<Candidate>>
    suspend fun incrementVote(candidateId: String): Candidate?
}