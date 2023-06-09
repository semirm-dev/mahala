package semir.mahovkic.mahala.data.room

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.Candidate

class CandidatesDao {
    private val candidates = mutableListOf<Candidate>()
    fun getCandidates(): Flow<List<Candidate>> {
        for (i in 1..50) {
            candidates.add(
                Candidate(
                    id = i,
                    name = "candidate-${i}",
                    profileImg = i,
                    party = if (i > 25) "party-a" else "party-b",
                    votes = 0
                )
            )
        }

        return MutableStateFlow(candidates)
    }

    fun incrementVote(candidateId: Int): Candidate? {
        return candidates.find { it.id == candidateId }?.apply {
            votes++
        }
    }
}