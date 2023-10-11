package com.example.messengerappdemo.model

import java.text.SimpleDateFormat
import java.util.*

data class UserProfile constructor(val id: Int,
                                   val name: String,
                                   val status: Boolean,
                                   val date: String,
                                   val pictureUrl: String,
                                   val userIP:String,
                                   val userPort:Int,
                                   val messages: List<MessageModel>
                                   )



val date = SimpleDateFormat("dd.MM.yyyy")
val strDate : String = date.format(Date())

val userProfileList = arrayListOf(
    UserProfile(
        id = 0,
        name = "Michaela Runnings",
        status = true,
        date = strDate,
        "https://images.unsplash.com/photo-1485290334039-a3c69043e517?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
        userIP = "10.10.50.10", //melih tel
        userPort = 9090,
        messages = mutableListOf<MessageModel>()
        ),
)