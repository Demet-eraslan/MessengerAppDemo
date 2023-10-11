package com.example.messengerappdemo.model

import android.os.AsyncTask
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.messengerappdemo.connection.Client
import com.example.messengerappdemo.connection.MulticastClient
import java.net.DatagramPacket
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class ChatViewModel : ViewModel() {
    val client = Client(9090)
    val mcClient = MulticastClient(2018);
    var chattingUsers: MutableState<List<UserModel>> = mutableStateOf(listOf())
    var mcGroups : MutableState<List<GroupModel>> = mutableStateOf(listOf())
    var userIdCounter: Int = 1
    var msgIdCounter: Int = 1
    var grpMsgCounter:Int=1
    var grpCounter:Int = 1
    val myLocalIP: String? = client.myIPAddress
    init {
        MyThread(start = true) {
            val isRunning = true
            while (isRunning) {
                //region ReceivingP2PMsg
                val dp: DatagramPacket = client.Receive()
                val senderIP: InetAddress = dp.address
                val senderPort: Int = dp.port
                val rcvMsg = String(dp.data, 0, dp.length)
                val newMessage = MessageModel(
                    msgIdCounter,
                    rcvMsg,
                    SimpleDateFormat("dd.MM.yyyy").format(Date()),
                    false,
                    false,
                    senderIP.toString().replace("/", ""),
                    senderPort
                )
                msgIdCounter++
                //endregion

                if (chattingUsers.value.isEmpty()) {
                    val newUser = UserModel(
                        userIdCounter,
                        "",
                        true,
                        senderIP.toString().replace("/", ""),
                        senderPort,
                        mutableStateOf(listOf(newMessage))
                    )
                    chattingUsers.value = chattingUsers.value + listOf(newUser)
                    userIdCounter++
                } else {
                    var isExist = false
                    run srch@{
                        chattingUsers.value.forEach {
                            if (it.userIP == newMessage.senderIP) {
                                it.messages.value = it.messages.value + newMessage
                                isExist = true
                                return@srch
                            }
                        }
                    }
                    if (!isExist) {
                        val newUser = UserModel(
                            userIdCounter,
                            "",
                            true,
                            senderIP.toString().replace("/", ""),
                            senderPort,
                            mutableStateOf(listOf(newMessage))
                        )
                        chattingUsers.value = chattingUsers.value + listOf(newUser)
                        userIdCounter++
                    }
                }
            }
        }

        MyThread(start = true) {

            mcClient.Join("224.1.1.1")
            val newGroup = GroupModel(grpCounter,"224.1.1.1",2018, mutableStateOf(listOf()))
            Thread.sleep(500)
            mcGroups.value = mcGroups.value + listOf(newGroup)

            var isRunning = true

            while (isRunning){
                val grpMessage = mcClient.ReceiveGroupMessage()
                val senderIP: InetAddress = grpMessage.address
                val senderPort: Int = grpMessage.port
                val rcvMsg = String(grpMessage.data, 0, grpMessage.length)

                if (rcvMsg == "LOGON"){
                    val newUser = UserModel(
                        userIdCounter,
                        "",
                        true,
                        senderIP.toString().replace("/", ""),
                        9090,
                        mutableStateOf(listOf())
                    )
                    var isLogged = false
                    run IsLogged@{
                        chattingUsers.value.forEach {
                            if (it.userIP == newUser.userIP) {
                                isLogged = true
                                return@IsLogged
                            }
                        }
                    }
                    if (newUser.userIP != myLocalIP && !isLogged){
                        chattingUsers.value = chattingUsers.value + listOf(newUser)
                        userIdCounter++
                        LogOnCallback()
                    }
                }
                else if (rcvMsg == "LOGOFF"){
                    chattingUsers.value.forEach { u ->
                        if (u.userIP == senderIP.toString().replace("/","")){
                            chattingUsers.value = chattingUsers.value - listOf(u)
                        }
                    }
                }
                else{
                    val gm = MessageModel(
                        grpMsgCounter,
                        rcvMsg,
                        SimpleDateFormat("dd.MM.yyyy").format(Date()),
                        false,
                        false,
                        senderIP.toString().replace("/", ""),
                        senderPort
                    )
                    grpMsgCounter++
                    var isExist = false
                    run srch2@{
                        mcGroups.value.forEach {
                            if (it.multicastIP == mcClient.GetMCAddress()) {
                                if (gm.senderIP != myLocalIP){
                                    it.messages.value = it.messages.value + gm
                                }
                                isExist = true
                                return@srch2
                            }
                        }
                    }
                }
            }
        }
    }

    fun MyThread(
        start: Boolean = true,
        isDaemon: Boolean = false,
        contextClassLoader: ClassLoader? = null,
        name: String? = null,
        priority: Int = -1,
        block: () -> Unit
    ): Thread {
        val thread = object : Thread() {
            override fun run() {
                block()
            }
        }
        if (isDaemon)
            thread.isDaemon = true
        if (priority > 0)
            thread.priority = priority
        if (name != null)
            thread.name = name
        if (contextClassLoader != null)
            thread.contextClassLoader = contextClassLoader
        if (start)
            thread.start()
        return thread
    }


    fun sendMessage(mssg: MessageModel, ip: String) {
        doAsync {
            client.SendMessage(mssg.msg, ip, 9090)
        }.execute()
    }
    fun sendGrpMessage(mssg: MessageModel) {
        doAsync {
            mcClient.SendGroupMessage(mssg.msg)
        }.execute()
    }
    fun LogOnCallback(){
        doAsync {
            mcClient.SendLogON()
        }.execute()
    }

    //async method structure
    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }
}