package semir.mahovkic.mahala.data

import kotlinx.coroutines.flow.Flow
import semir.mahovkic.mahala.data.room.CandidatesDao

class CandidatesRepository(private val candidatesDao: CandidatesDao) {
    fun getCandidates(): Flow<List<Candidate>> {
        return candidatesDao.getCandidates()
    }

    fun incrementVote(candidateId: Int): Candidate? {
        return candidatesDao.incrementVote(candidateId)
    }
}