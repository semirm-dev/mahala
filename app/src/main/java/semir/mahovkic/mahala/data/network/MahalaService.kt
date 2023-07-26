package semir.mahovkic.mahala.data.network

import semir.mahovkic.mahala.data.PartiesApi
import semir.mahovkic.mahala.data.VotesApi
import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import semir.mahovkic.mahala.data.model.Party
import semir.mahovkic.mahala.data.network.model.CandidateDetailsDto
import semir.mahovkic.mahala.data.network.model.CandidateDto
import semir.mahovkic.mahala.data.network.model.PartyDto
import semir.mahovkic.mahala.data.network.model.SendVoteDto
import javax.inject.Inject

class MahalaService @Inject constructor(
    private val api: MahalaApi
) : VotesApi, PartiesApi {

    override suspend fun getCandidates(): List<Candidate> {
        return api.getCandidates().map { response -> response.toCandidate() }
    }

    override suspend fun getCandidateDetails(candidateId: String): CandidateDetails {
        return api.getCandidateDetails(candidateId).toCandidateDetails()
    }

    override suspend fun vote(voterId: String, candidateId: String, groupId: Int): String =
        api.vote(SendVoteDto(voterId, candidateId, groupId)).message

    override suspend fun getParties(): List<Party> {
        return api.getParties().map { response -> response.toParty() }
    }
}

fun CandidateDto.toCandidate(): Candidate = Candidate(
    id = id,
    name = name,
    votingNumber = votingNumber,
    profileImg = profileImg,
    gender = gender,
    party = party.toParty(),
)

fun CandidateDetailsDto.toCandidateDetails(): CandidateDetails = CandidateDetails(
    id = id,
    name = name,
    votingNumber = votingNumber,
    profileImg = profileImg,
    gender = gender,
    party = party.toParty(),
    totalVotes = totalVotes
)

fun PartyDto.toParty(): Party = Party(
    id = id,
    name = name
)