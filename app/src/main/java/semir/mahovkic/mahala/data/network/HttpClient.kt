package semir.mahovkic.mahala.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getClient(): Retrofit {
//    val interceptor = HttpLoggingInterceptor()
//    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    return Retrofit.Builder()
        .baseUrl("http://a12e-37-203-97-187.ngrok-free.app/api/")
        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
        .build()
}