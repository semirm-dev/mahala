package semir.mahovkic.mahala.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.VotesApi

class MahalaService : VotesApi {

    override fun getCandidatesStream(): Flow<List<Candidate>> {
        val candidates = mutableListOf<Candidate>()

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

    override fun incrementVote(candidateId: Int): Candidate? {
        TODO("Not yet implemented")
    }
}