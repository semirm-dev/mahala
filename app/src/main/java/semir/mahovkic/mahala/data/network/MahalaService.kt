package semir.mahovkic.mahala.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.VotesApi
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import semir.mahovkic.mahala.data.model.CandidateVote
import semir.mahovkic.mahala.data.network.model.SendVoteDto
import javax.inject.Inject

class MahalaService @Inject constructor() : VotesApi {
    private val _candidates = mutableListOf<Candidate>()
    private val _api = getClient().create(MahalaApi::class.java)

    override suspend fun getCandidatesStream(): Flow<List<Candidate>> {
        val resp = _api.getCandidates().map { response -> response.toCandidate() }

        _candidates.addAll(resp)

        return MutableStateFlow(_candidates)
    }

    override suspend fun getCandidateDetails(candidateId: String): CandidateDetails {
        val resp = _api.getCandidateDetails(candidateId)
        return CandidateDetails(votes = resp.map { it.toCandidateVote() })
    }

    override suspend fun vote(candidateId: String, voterId: String) =
        _api.vote(SendVoteDto(candidateId, voterId))
}

fun semir.mahovkic.mahala.data.network.model.CandidateDto.toCandidate(): Candidate = Candidate(
    id = id,
    name = name,
    profileImg = profileImg ?: 0,
    party = party ?: "",
)

fun semir.mahovkic.mahala.data.network.model.CandidateVoteDto.toCandidateVote(): CandidateVote = CandidateVote(
    candidateId = candidateID,
    voterId = voterID
)