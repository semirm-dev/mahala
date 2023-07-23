package semir.mahovkic.mahala.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import semir.mahovkic.mahala.data.network.model.CandidateDetailsDto
import semir.mahovkic.mahala.data.network.model.CandidateDto
import semir.mahovkic.mahala.data.network.model.PartyDto
import semir.mahovkic.mahala.data.network.model.SendVoteDto
import semir.mahovkic.mahala.data.network.model.VoteResponseDto

interface MahalaApi {
    @GET("candidates")
    suspend fun getCandidates(): List<CandidateDto>

    @GET("candidates/{id}")
    suspend fun getCandidateDetails(@Path("id") candidateId: String): CandidateDetailsDto

    @POST("votes")
    suspend fun vote(@Body votingTicket: SendVoteDto): VoteResponseDto

    @GET("parties")
    suspend fun getParties(): List<PartyDto>
}