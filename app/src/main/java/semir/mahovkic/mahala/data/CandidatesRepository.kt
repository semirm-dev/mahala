package semir.mahovkic.mahala.data

import kotlinx.coroutines.flow.Flow
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

class CandidatesRepository @Inject constructor (
    private val votesRemoteDataSource: VotesRemoteDataSource,
    private val localDataStore: VotesLocalDataStore
) {
    suspend fun getCandidatesStream(): Flow<List<Candidate>> = votesRemoteDataSource.getCandidatesStream()

    suspend fun incrementVote(candidateId: Int): Candidate? = votesRemoteDataSource.incrementVote(candidateId)
}