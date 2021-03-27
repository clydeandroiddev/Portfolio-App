package com.tawktest.takehomeexam.data.network

import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.data.db.entities.UserListTable
import com.tawktest.takehomeexam.data.db.entities.UserProfileTable
import com.tawktest.takehomeexam.model.UserProfileData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface ApiCalls {


    @GET("users")
    suspend fun userlist(
        @Query("since") id : Int
    ): Response<List<UserListTable>>

    @GET
    suspend fun userprofile(
       @Url url : String
    ): Response<UserProfileTable>

    companion object {
        operator  fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : ApiCalls {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()



            return Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiCalls::class.java)
        }
    }



}