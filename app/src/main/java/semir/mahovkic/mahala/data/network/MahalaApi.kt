package semir.mahovkic.mahala.data.network

import retrofit2.http.GET
import retrofit2.http.POST
import semir.mahovkic.mahala.data.network.model.CandidateResponse

interface MahalaApi {
    @GET("candidates")
    suspend fun getCandidates(): List<CandidateResponse>
    @POST("votes")
    suspend fun vote(candidateID: String, voterID: String)
}