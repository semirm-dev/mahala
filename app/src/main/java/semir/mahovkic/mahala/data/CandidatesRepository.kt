package semir.mahovkic.mahala.data

import kotlinx.coroutines.flow.Flow
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

class CandidatesRepository @Inject constructor(
    private val votesRemoteDataSource: VotesRemoteDataSource,
) {
    suspend fun getCandidatesStream(): Flow<List<Candidate>> =
        votesRemoteDataSource.getCandidatesStream()

    suspend fun vote(candidateId: String, voterId: String) =
        votesRemoteDataSource.vote(candidateId, voterId)
}