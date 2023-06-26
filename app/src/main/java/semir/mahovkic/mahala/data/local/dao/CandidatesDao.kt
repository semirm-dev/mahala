package semir.mahovkic.mahala.data.local.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.local.entities.CandidateEntity

class CandidatesDao {
    private val _candidates = mutableListOf<CandidateEntity>()

    fun getCandidates(): Flow<List<CandidateEntity>> {
        return MutableStateFlow(_candidates);
    }

    fun incrementVote(candidateId: Int): CandidateEntity? {
        return _candidates.find { it.id == candidateId }?.apply {
            votes++
        }
    }
}