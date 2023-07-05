package semir.mahovkic.mahala.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import semir.mahovkic.mahala.data.network.model.CandidateVoteDto
import semir.mahovkic.mahala.data.network.model.CandidateDto
import semir.mahovkic.mahala.data.network.model.SendVoteDto

interface MahalaApi {
    @GET("candidates")
    suspend fun getCandidates(): List<CandidateDto>

    @GET("votes")
    suspend fun getCandidateDetails(@Query("candidateID") candidateId: String): List<CandidateVoteDto>

    @POST("votes")
    suspend fun vote(@Body votingTicket: SendVoteDto)
}