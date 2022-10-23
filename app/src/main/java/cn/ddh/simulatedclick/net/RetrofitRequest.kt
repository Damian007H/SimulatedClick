package cn.ddh.simulatedclick.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRequest {
    private var apiService: ApiService? = null
    private var okHttpClient: OkHttpClient? = null

    fun getInstance(): ApiService {
        if (apiService == null) {
            okHttpClient = OkHttpClient.Builder().build()
            apiService = Retrofit.Builder().baseUrl("https://plamshop.net/addons/track/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient!!).build().create(ApiService::class.java)
        }

        return apiService!!

    }


}