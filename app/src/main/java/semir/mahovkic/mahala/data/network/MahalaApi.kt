package semir.mahovkic.mahala.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import semir.mahovkic.mahala.data.network.model.CandidatesResponse
import semir.mahovkic.mahala.data.network.model.SendVoteRequest

interface MahalaApi {
    @GET("candidates")
    suspend fun getCandidates(): List<CandidatesResponse>
    @POST("votes")
    suspend fun vote(@Body votingTicket: SendVoteRequest)
}