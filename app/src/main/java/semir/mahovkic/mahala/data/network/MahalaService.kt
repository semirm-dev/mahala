package semir.mahovkic.mahala.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.VotesApi
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

class MahalaService @Inject constructor() : VotesApi {
    private val _candidates = mutableListOf<Candidate>()

    override fun getCandidatesStream(): Flow<List<Candidate>> {
        for (i in 1..50) {
            _candidates.add(
                Candidate(
                    id = i,
                    name = "candidate-${i}",
                    profileImg = i,
                    party = if (i > 25) "party-a" else "party-b",
                    votes = 0
                )
            )
        }

        return MutableStateFlow(_candidates)
    }

    override fun incrementVote(candidateId: Int): Candidate? {
        return _candidates.find { it.id == candidateId }?.apply {
            votes++
        }
    }
}