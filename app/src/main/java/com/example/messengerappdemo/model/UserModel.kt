package com.example.messengerappdemo.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class UserModel (val id: Int,
                      val name: String,
                      val status: Boolean,
                      val userIP:String,
                      val userPort:Int,
                      val messages: MutableState<List<MessageModel>>
                      ){
    constructor(): this(-1,"",false,"",-1, mutableStateOf(listOf()))
}

