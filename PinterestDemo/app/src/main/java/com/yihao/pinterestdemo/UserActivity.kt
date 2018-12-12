package com.yihao.pinterestdemo

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yihao.dribbbledemo.httpservices.LoginService
import com.yihao.dribbbledemo.httpservices.UserService
import com.yihao.pinterestdemo.dto.Board
import com.yihao.pinterestdemo.dto.Image
import com.yihao.pinterestdemo.dto.Token
import com.yihao.pinterestdemo.dto.User
import com.yihao.pinterestdemo.modules.user.BoardFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserActivity : FragmentActivity() {

    private var tvName: TextView? = null
    private var ivAvatar: ImageView? = null
    private var boardsFragment: BoardFragment?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        tvName = findViewById(R.id.tv_name)
        ivAvatar = findViewById(R.id.iv_avatar)
        boardsFragment = BoardFragment()
        supportFragmentManager.beginTransaction().replace(R.id.board_container, boardsFragment!!).commit()
        boardsFragment!!.setBoards(makeTestBoards())
        var code = intent.getStringExtra("code")
        getUser(code)


    }

    private fun makeTestBoards(): MutableList<Board> {
        var boards: MutableList<Board> = ArrayList()
        boards.add(Board("0", "test1", Image("", 0, 0)))
        boards.add(Board("1", "test2", Image("", 0, 0)))
        boards.add(Board("2", "test3", Image("", 0, 0)))
        boards.add(Board("3", "test4", Image("", 0, 0)))
        boards.add(Board("4", "test5", Image("", 0, 0)))
        boards.add(Board("5", "test6", Image("", 0, 0)))
        boards.add(Board("6", "test7", Image("", 0, 0)))
        return boards
    }

    private fun getUser(code: String) {
        val loginService = LoginService.create()
        loginService.getToken(
            clientId = Constants.CLIENT_ID,
            clientSecret = Constants.CLIENT_SECRET, code = code
        ).observeOn(Schedulers.newThread()).subscribeOn(Schedulers.io())
            .subscribe { token: Token ->
                getUserData(token.access_token)
            }
    }

    private fun getUserData(accessToken: String) {
        val userService = UserService.create()

        Observable.merge(userService.getCurrentUser(accessToken), userService.getMyBoards(accessToken))
            .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe({ it ->
                if (it is User) {
                    tvName!!.text = it.username
                    Glide.with(this@UserActivity).load(it.image["60x60"]?.url).into(ivAvatar!!)
                } else if (it is List<*>) {
                    try {
                        boardsFragment!!.setBoards(it as List<Board>)
                    }catch (exp:ClassCastException){

                    }

                }
            },
                {
                    it.printStackTrace()
                })

    }
}
