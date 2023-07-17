package semir.mahovkic.mahala.data.network

import semir.mahovkic.mahala.data.VotesApi
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import semir.mahovkic.mahala.data.model.CandidateVote
import semir.mahovkic.mahala.data.network.model.CandidateDetailsDto
import semir.mahovkic.mahala.data.network.model.CandidateDto
import semir.mahovkic.mahala.data.network.model.CandidateVoteDto
import semir.mahovkic.mahala.data.network.model.SendVoteDto
import javax.inject.Inject

class MahalaService @Inject constructor() : VotesApi {
    private val _api = getClient().create(MahalaApi::class.java)

    override suspend fun getCandidatesStream(): List<Candidate> {
        return _api.getCandidates().map { response -> response.toCandidate() }
    }

    override suspend fun getCandidateDetails(candidateId: String): CandidateDetails {
        val resp = _api.getCandidateDetails(candidateId)
        return resp.toCandidateDetails()
    }

    override suspend fun vote(candidateId: String, voterId: String) =
        _api.vote(SendVoteDto(candidateId, voterId))
}

fun CandidateDto.toCandidate(): Candidate = Candidate(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
)

fun CandidateDetailsDto.toCandidateDetails(): CandidateDetails = CandidateDetails(
    id = id,
    name = name,
    profileImg = profileImg,
    party = party,
    votes = votes?.map { it.toCandidateVote() }
)

fun CandidateVoteDto.toCandidateVote(): CandidateVote = CandidateVote(
    voterId = voterID,
)