package com.yihao.dribbbledemo.httpservices

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yihao.pinterestdemo.dto.Token
import com.yihao.pinterestdemo.Constants
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by 易昊 on 2018/12/5.
 */
interface LoginService {
    @POST("v1/oauth/token")
    fun getToken(@Query("grant_type") grantType: String = "authorization_code", @Query("client_id") clientId: String, @Query("client_secret") clientSecret: String,
                 @Query("code") code:String):Observable<Token>

    companion object Factory {
        fun create(): LoginService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.SERVER_IP)
                .build()

            return retrofit.create(LoginService::class.java)
        }
    }
}