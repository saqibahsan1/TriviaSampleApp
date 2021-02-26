package com.saqib.triviasample.httpClient
import com.saqib.triviasample.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object{
        private var instance: HttpEndPoints? = null

        fun getInstance(): HttpEndPoints? {
            if (instance == null) {
                lateinit var retrofit: Retrofit

                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    val httpClient = OkHttpClient.Builder()
                    httpClient.addInterceptor(logging)
                    retrofit = Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build()

                instance = retrofit.create(HttpEndPoints::class.java)
            }
            return instance
        }
    }
}