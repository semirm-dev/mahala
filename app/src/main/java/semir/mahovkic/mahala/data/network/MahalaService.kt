package semir.mahovkic.mahala.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.VotesApi
import semir.mahovkic.mahala.data.model.Candidate
import javax.inject.Inject

class MahalaService @Inject constructor() : VotesApi {
    private val _candidates = mutableListOf<Candidate>()
    private val _api = getClient().create(MahalaApi::class.java)

    override suspend fun getCandidatesStream(): Flow<List<Candidate>> {
        val resp = _api.getCandidates().map { response ->
            Candidate(response.id, response.name, 0, "", 0)
        }

        _candidates.addAll(resp)

        return MutableStateFlow(_candidates)
    }

    override suspend fun incrementVote(candidateId: String): Candidate? {
        return _candidates.find { it.id == candidateId }?.apply {
            votes++
        }
    }
}