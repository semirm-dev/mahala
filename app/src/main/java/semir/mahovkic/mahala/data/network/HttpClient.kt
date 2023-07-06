package semir.mahovkic.mahala.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import semir.mahovkic.mahala.BuildConfig

fun getClient(): Retrofit {
//    val interceptor = HttpLoggingInterceptor()
//    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val votingServiceApi = BuildConfig.VOTING_SERVICE_API

    return Retrofit.Builder()
        .baseUrl(votingServiceApi)
        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
        .build()
}