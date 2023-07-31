package semir.mahovkic.mahala.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import javax.inject.Inject

class CandidatesRepository @Inject constructor(
    private val candidatesRemoteDataSource: CandidatesRemoteDataSource,
) {
    private val _candidates = mutableListOf<Candidate>()

    suspend fun getCandidatesStream(): Flow<List<Candidate>> {
        val candidates = candidatesRemoteDataSource.getCandidatesStream()
        _candidates.clear()
        _candidates.addAll(candidates)
        return MutableStateFlow(_candidates)
    }

    suspend fun getCandidateDetails(candidateId: String): CandidateDetails =
        candidatesRemoteDataSource.getCandidateDetails(candidateId)

    suspend fun vote(voterId: String, candidateId: String, groupId: Int) =
        candidatesRemoteDataSource.vote(voterId, candidateId, groupId)
}