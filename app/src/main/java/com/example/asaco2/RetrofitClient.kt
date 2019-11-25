package com.example.asaco2

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val MY_SERVER_URL = ""

    //Retrofit instance
    fun myServerMethods(): MyServerMethod {
        val retrofit = Retrofit.Builder()
            .baseUrl(MY_SERVER_URL)
            .client(OkHttpClient().newBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MyServerMethod::class.java)
    }

}

interface MyServerMethod {
    @GET("json_create")
    fun getEntity(
        @Query("name") name: String,
        @Query("calory") calory: String
    ): Call<Food>
}

data class Food(
    val name: String,
    val calory: String
) {

    //HttpClient
    val httpBuilder: OkHttpClient.Builder
        get() {
            val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
                chain.request().newBuilder()
                    .header("Accept", "/json_create")
                    .build().let { chain.proceed(it) }
            }.readTimeout(30, TimeUnit.SECONDS)

            HttpLoggingInterceptor().let {
                it.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(it)
            }
            return httpClient
        }
}