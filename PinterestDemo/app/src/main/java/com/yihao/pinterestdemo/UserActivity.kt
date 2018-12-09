package com.yihao.pinterestdemo

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.yihao.pinterestdemo.dto.Token
import com.yihao.dribbbledemo.httpservices.LoginService
import com.yihao.dribbbledemo.httpservices.UserService
import com.yihao.pinterestdemo.MainActivity.Companion.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserActivity : Activity() {

    private var tvName: TextView? = null
    private var ivAvatar: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        tvName = findViewById(R.id.tv_name)
        ivAvatar = findViewById(R.id.iv_avatar)
        var code = intent.getStringExtra("code")
        getUser(code)
    }

    private fun getUser(code: String) {
        val userService = UserService.create()
        val loginService = LoginService.create()
        loginService.getToken(
            clientId = Constants.CLIENT_ID,
            clientSecret = Constants.CLIENT_SECRET, code = code
        ).observeOn(Schedulers.newThread()).subscribeOn(Schedulers.io())
            .flatMap { token: Token ->
                getSharedPreferences("http_preference", Context.MODE_PRIVATE).edit().putString("access_token",
                    token.access_token).commit()
                userService.getCurrentUser(token.access_token)
            }.observeOn(Schedulers.newThread()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ response ->
                Log.d(TAG, response.toString())
                tvName?.text = response.username
                Glide.with(this@UserActivity).load(response.image["60x60"]?.url).into(ivAvatar!!)
            }, {
                it.printStackTrace()
                Toast.makeText(this@UserActivity, "获取用户信息失败", Toast.LENGTH_SHORT).show()
            })
    }
}
