package com.yihao.pinterestdemo.dto

/**
 * Created by 易昊 on 2018/12/5.
 */
data class User (var id:String, var username:String, var image:Map<String, Image>)