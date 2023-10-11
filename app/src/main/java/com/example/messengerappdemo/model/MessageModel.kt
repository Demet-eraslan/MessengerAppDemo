package com.example.messengerappdemo.model

import java.net.InetAddress


data class MessageModel (
    var msgID: Int,
    var msg: String,
    var msgDate: String,
    var msgIsSent: Boolean,
    var msgIsRead: Boolean,
    var senderIP: String,
    var senderPort: Int,
    ){
    constructor():this(-1,"","",false,false,"",-1)
}