package semir.mahovkic.mahala.data.local.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.local.entities.CandidateEntity
import javax.inject.Inject

class CandidatesDao @Inject constructor() {
    private val _candidates = mutableListOf<CandidateEntity>()

    fun getCandidates(): Flow<List<CandidateEntity>> {
        TODO("not implemented")
    }

    fun incrementVote(candidateId: String): CandidateEntity? {
        TODO("not implemented")
    }
}