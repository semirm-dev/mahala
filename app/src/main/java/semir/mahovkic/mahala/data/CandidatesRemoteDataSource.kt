package semir.mahovkic.mahala.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import javax.inject.Inject

class CandidatesRemoteDataSource @Inject constructor(
    private val api: VotingApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {


    suspend fun getCandidatesStream(): List<Candidate> =
        withContext(ioDispatcher) {
            api.getCandidates()
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
