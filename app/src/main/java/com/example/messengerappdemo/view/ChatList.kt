package com.example.messengerappdemo.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.messengerappdemo.R
import com.example.messengerappdemo.model.*
import com.example.messengerappdemo.ui.theme.*
import com.example.messengerappdemo.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


var remoteUser = UserModel()

// CHAT PAGE MAIN STRUCTURE
@SuppressLint("SimpleDateFormat")
@Composable
fun ChatList(userId: Int, navController: NavHostController) {

    val menuExpanded = remember { mutableStateOf(false) }

    viewModel.chattingUsers.value.forEach {
        if (it.id == userId) {
            remoteUser = it
        }
    }

    val topChatBar: @Composable () -> Unit = {

        TopAppBar(

            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = { navController?.navigateUp() })
                    {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back Arrow",
                            tint = Color.White
                        )
                    }

                    //ProfilePicture(pictureUrl = userProfile.pictureUrl, onlineStatus = userProfile.status, imageSize = 32.dp)

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = remoteUser.userIP,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_videocam_24),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_call),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    menuExpanded.value = true
                }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false },
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopStart)
                    ) {
                        DropdownMenuItem(onClick = { }) {
                            Text(text = "Blokla")
                        }
                        DropdownMenuItem(onClick = {
                            navController.navigate("whats_app_detail/${remoteUser.id}")
                        }) {
                            Text(text = "Kişi bilgisi")
                        }

                    }
                }
            },
            backgroundColor = MainBlue,
            elevation = AppBarDefaults.TopAppBarElevation
        )
    }
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.background_image),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Scaffold(
            backgroundColor = Color.Transparent,
            topBar = {
                topChatBar()
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        LazyColumn {
                            items(remoteUser.messages.value) { msg ->
                                ChatListItem(msg)
                                Box(Modifier.size(width = 0.dp, height = 2.dp)) {
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                BottomDesign()
            }
        )
    }
}

//TEXT FIELD AREA
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomDesign() {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    //var message: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var expanded: Boolean by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    Row(
        modifier = Modifier
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(0.85f)
                .wrapContentSize()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp, 0.dp, 10.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = expanded) {
                Icon(
                    imageVector = Icons.Rounded.NavigateNext,
                    contentDescription = "openbar",
                    Modifier
                        .padding(8.dp, 4.dp, 4.dp, 10.dp)
                        .clickable(onClick = { expanded = false })
                )
            }
            AnimatedVisibility(visible = !expanded) {
                Row() {
                    Icon(
                        imageVector = Icons.Rounded.AttachFile,
                        contentDescription = "Camera",
                        Modifier
                            .padding(8.dp, 13.dp, 4.dp, 10.dp)
                            .clickable(onClick = { })
                    )
                    Icon(
                        imageVector = Icons.Rounded.Image,
                        contentDescription = "New Album",
                        Modifier
                            .padding(8.dp, 13.dp, 4.dp, 4.dp)
                            .clickable(onClick = { })
                    )
                    Icon(
                        imageVector = Icons.Rounded.EmojiEmotions,
                        contentDescription = "New Album",
                        Modifier
                            .padding(8.dp, 13.dp, 4.dp, 4.dp)
                            .clickable(onClick = { })
                    )
                }
            }
            TextField(
                value = textState.value,
                onValueChange = {
                    textState.value = it
                    expanded = true
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                },
                placeholder = {
                    Text(
                        text = "Mesaj...",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier
                    .weight(0.66f)
                    .wrapContentHeight()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(400) // delay to way the keyboard shows up
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Text,
                    autoCorrect = true,
                    imeAction = ImeAction.Done
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                ),
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
        Row(
            modifier = Modifier.weight(0.15f),
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                onClick = {
                    //SEND FUNCTION
                    if (textState.value.text.trim() != "") {
                        val msg = MessageModel(
                            viewModel.msgIdCounter,
                            textState.value.text.trim(),
                            SimpleDateFormat("dd.MM.yyyy").format(Date()),
                            false,
                            false,
                            viewModel.client.myIPAddress,
                            9090
                        )
                        viewModel.sendMessage(msg, remoteUser.userIP)
                        viewModel.msgIdCounter++
                        remoteUser.messages.value = remoteUser.messages.value + msg
                        textState.value = TextFieldValue()
                    }
                },
                backgroundColor = MainBlue
            ) {
                Icon(
                    painter = painterResource(
                        if (textState.value.text.isEmpty()) {
                            R.drawable.ic_voice_record
                        } else {
                            R.drawable.ic_baseline_send_24
                        }
                    ),
                    contentDescription = "Text Icon",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

// MESSAGE BOXES
@Composable
fun ChatListItem(data: MessageModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (data.senderIP != viewModel.myLocalIP) {//sent msg box
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(WhatsAppOutgoingMsg)
                    .padding(5.dp)
            ) {

                Text(
                    text = data.senderIP,
                    color = MainBlueInfo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = data.msg,
                    color = Color.Black,
                    fontSize = 15.sp
                )
                Text(
                    text = data.msgDate,
                    color = Color.LightGray,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {//received msg box
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(5.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    text = data.msg,
                    color = Color.Black,
                    fontSize = 15.sp
                )
                Text(
                    text = data.msgDate,
                    color = Color.LightGray,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    ChatList(0, navController = NavHostController(LocalContext.current))
}

