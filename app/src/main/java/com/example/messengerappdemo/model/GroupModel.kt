package com.example.messengerappdemo.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.messengerappdemo.connection.MulticastClient

data class GroupModel(val id:Int,
                      val multicastIP:String,
                      val multicastPort:Int,
                      val messages: MutableState<List<MessageModel>>
                      ){
    constructor(): this(-1,"",-1, mutableStateOf(listOf()))
}
