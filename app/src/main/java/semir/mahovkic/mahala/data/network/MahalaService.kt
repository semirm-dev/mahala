package semir.mahovkic.mahala.data.network

import semir.mahovkic.mahala.data.PartiesApi
import semir.mahovkic.mahala.data.VotesApi
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import semir.mahovkic.mahala.data.model.CandidateVote
import semir.mahovkic.mahala.data.model.Party
import semir.mahovkic.mahala.data.network.model.CandidateDetailsDto
import semir.mahovkic.mahala.data.network.model.CandidateDto
import semir.mahovkic.mahala.data.network.model.CandidateVoteDto
import semir.mahovkic.mahala.data.network.model.PartyDto
import semir.mahovkic.mahala.data.network.model.SendVoteDto
import javax.inject.Inject

class MahalaService @Inject constructor() : VotesApi, PartiesApi {
    private val _api = getClient().create(MahalaApi::class.java)

    override suspend fun getCandidates(): List<Candidate> {
        return _api.getCandidates().map { response -> response.toCandidate() }
    }

    override suspend fun getCandidateDetails(candidateId: String): CandidateDetails {
        return _api.getCandidateDetails(candidateId).toCandidateDetails()
    }

    override suspend fun vote(candidateId: String, voterId: String): String =
        _api.vote(SendVoteDto(candidateId, voterId)).message

    override suspend fun getParties(): List<Party> {
        return _api.getParties().map { response -> response.toParty() }
    }
}

fun CandidateDto.toCandidate(): Candidate = Candidate(
    id = id,
    name = name,
    votingNumber = votingNumber,
    profileImg = profileImg,
    gender = gender,
    party = party,
)

fun CandidateDetailsDto.toCandidateDetails(): CandidateDetails = CandidateDetails(
    id = id,
    name = name,
    votingNumber = votingNumber,
    profileImg = profileImg,
    gender = gender,
    party = party,
    votes = votes?.map { it.toCandidateVote() }
)

fun CandidateVoteDto.toCandidateVote(): CandidateVote = CandidateVote(
    voterId = voterID,
)

fun PartyDto.toParty(): Party = Party(
    name = name
)