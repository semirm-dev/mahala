package semir.mahovkic.mahala.data

import semir.mahovkic.mahala.data.model.Candidate
import semir.mahovkic.mahala.data.model.CandidateDetails
import semir.mahovkic.mahala.data.model.Group
import semir.mahovkic.mahala.data.model.Party

interface VotingApi {
    suspend fun getCandidates(): List<Candidate>
    suspend fun getCandidateDetails(candidateId: String): CandidateDetails
    suspend fun vote(voterId: String, candidateId: String, groupId: Int): String
    suspend fun getParties(): List<Party>
    suspend fun getGroups(): List<Group>
}