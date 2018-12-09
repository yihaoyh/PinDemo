package com.yihao.dribbbledemo.httpservices

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yihao.pinterestdemo.dto.User
import com.yihao.pinterestdemo.Constants
import com.yihao.pinterestdemo.httpservices.PinConverterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by 易昊 on 2018/12/5.
 */

//                            val urlBuilder = HttpUrl.parse("https://api.dribbble.com/v2/user")!!.newBuilder()
////                            urlBuilder.addQueryParameter("access_token", accessToken)
//                            val request = Request.Builder().url(urlBuilder.build()).get().addHeader(
//                                "Authorization",
//                                "Bearer $accessToken"
//                            ).build()
interface UserService {
    @GET("v1/me")
    fun getCurrentUser(@Query("access_token") accessToken: String, @Query("fields") fields:String
     ="username, id, image"):Observable<User>

    companion object Factory {
        fun create(): UserService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(PinConverterFactory.create())
                .baseUrl(Constants.API_IP)
                .build()

            return retrofit.create(UserService::class.java)
        }
    }
}