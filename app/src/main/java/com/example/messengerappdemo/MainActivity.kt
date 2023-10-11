package com.example.messengerappdemo

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.messengerappdemo.model.ChatViewModel
import com.example.messengerappdemo.model.UserModel
import com.example.messengerappdemo.ui.theme.*
import com.example.messengerappdemo.utils.Constants._tabCurrentStatus
import com.example.messengerappdemo.utils.Constants.tabCurrentStatus
import com.example.messengerappdemo.view.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


// Main Class sayfalar arası geçişlerin yönetimi
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState)
        setContent {
            MessengerAppDemoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "whats_app_main"
                ) {
                    composable("whats_app_main") { MainAppActivity(navController) }
                    composable("user_settings") { Settings(navController) }
                    composable(
                        route = "user_chat/{userId}",
                        arguments = listOf(navArgument("userId") {
                            type = NavType.IntType
                        })
                    ) { navBackStackEntry ->
                        ChatList(navBackStackEntry.arguments!!.getInt("userId"), navController)
                    }
                    composable(
                        route = "grp_chat/{grpId}",
                        arguments = listOf(navArgument("grpId") {
                            type = NavType.IntType
                        })
                    ) { navBackStackEntry ->
                        GroupChatList(navBackStackEntry.arguments!!.getInt("grpId"), navController)
                    }
                    composable("groups") {
                        GroupScreen(navController)
                    }
                    composable("users_list") {
                        UserListScreen(navController)
                    }
                    composable(
                        route = "whats_app_detail/{userId}",
                        arguments = listOf(navArgument("userId") {
                            type = NavType.IntType
                        })
                    ) { navBackStackEntry ->
                        UserProfileDetailsScreen(
                            navBackStackEntry.arguments!!.getInt("userId"),
                            navController
                        )
                    }
                }
            }
        }
    }
}

var viewModel: ChatViewModel = ChatViewModel()


@ExperimentalPagerApi
@Composable
fun MainAppActivity(navController: NavHostController) {
    val context = LocalContext.current
    val menuExpanded = remember { mutableStateOf(false) }
    val tabStatus = tabCurrentStatus.observeAsState()
    val openDialog = remember { mutableStateOf(false) }

    val topBar: @Composable () -> Unit = {
        TopAppBar(
            title = {
                Text(
                    text = "HUY(IP:" + viewModel.myLocalIP + ")",
                    color = Color.White,
                    fontSize = 20.sp
                )
            },

            actions = {

                IconButton(
                    onClick = {
                        menuExpanded.value = true
                    }
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    DropdownMenu(
                        modifier = Modifier
                            .width(200.dp)
                            .wrapContentSize(Alignment.TopStart),
                        expanded = menuExpanded.value,
                        onDismissRequest = {
                            menuExpanded.value = false
                        }
                    ) {
                        when (tabStatus.value) {
                            0 -> {
                                DropdownMenuItem(onClick = { /*Handle Settings*/ }) {
                                    Text(text = "Sohbet Ayarları")
                                }
                            }
                            1 -> {
                                DropdownMenuItem(onClick = { /*Handle Settings*/ }) {
                                    Text(text = "Grup Ayarları")
                                }
                            }
                            2 -> {
                                DropdownMenuItem(onClick = { /*Handle Settings*/ }) {
                                    Text(text = "Arama Kayıtlarını Sil")
                                }
                            }
                        }
                        DropdownMenuItem(onClick = { navController.navigate("user_settings") }) {
                            Text(text = "Ayarlar")
                        }
                    }
                }

            },
            backgroundColor = DarkBlue,
            elevation = AppBarDefaults.TopAppBarElevation
        )
    }

    val dialog: @Composable () -> Unit = {
        var newIP by remember { mutableStateOf("") }
        var alertMsg by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "IP Adresi Girin")
            },
            text = {
                Column() {
                    TextField(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        value = newIP,
                        onValueChange = { newIP = it }

                    )
                    Text(alertMsg)
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (newIP != viewModel.myLocalIP) {
                                if (viewModel.client.ValidateIP(newIP)) {
                                    var remUser = UserModel(
                                        viewModel.userIdCounter,
                                        "",
                                        true,
                                        newIP,
                                        9090,
                                        mutableStateOf(listOf())
                                    )
                                    var isExist = false
                                    run srch@{
                                        viewModel.chattingUsers.value.forEach {
                                            if (it.userIP == remUser.userIP) {
                                                remUser = it
                                                isExist = true
                                                return@srch
                                            }
                                        }
                                    }
                                    if (!isExist) {
                                        viewModel.chattingUsers.value =
                                            viewModel.chattingUsers.value + remUser
                                        viewModel.userIdCounter++
                                        navController?.navigate("user_chat/${remUser.id}")
                                    } else {
                                        navController?.navigate("user_chat/${remUser.id}")
                                    }
                                    openDialog.value = false
                                }
                                else{
                                    alertMsg = "Geçerli bir IPv4 Adresi girin."
                                }
                            }
                            else{
                                alertMsg = "Kendinize mesaj gönderemezsiniz!"
                            }
                        }
                    ) {
                        Text("Mesaj Gönder")
                    }
                }
            }
        )
    }
    if (openDialog.value) {
        dialog()
    }
    Scaffold(
        topBar = {
            topBar()
        },
        content = {
            WhatsAppTab(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (tabStatus.value) {
                        0 -> {
                            openDialog.value = true
                        }
                        1 -> {
                            Toast.makeText(context, "Yeni Grup", Toast.LENGTH_SHORT).show()
                        }
                        2 -> {
                            Toast.makeText(context, "Yeni Çağrı", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                backgroundColor = MainPink,
                elevation = FloatingActionButtonDefaults.elevation(),
                modifier = Modifier.padding(10.dp),
            ) {
                when (tabStatus.value) {
                    0 -> {
                        Icon(
                            painterResource(id = R.drawable.ic_chat),
                            contentDescription = "Message",
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    1 -> {
                        Icon(
                            painterResource(id = R.drawable.ic_groups),
                            contentDescription = "Group",
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    2 -> {
                        Icon(
                            painterResource(id = R.drawable.ic_call),
                            contentDescription = "Add Call",
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    )
}

// START OF TABS SWITCHING CONTROLS
@ExperimentalPagerApi
@Composable
fun WhatsAppTab(navController: NavHostController) {
    val pagerState = rememberPagerState(pageCount = 3)
    Column {
        Tabs(pagerState)
        TabsContent(pagerState, navController)
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf("SOHBET", "GRUP", "ARAMA")
    val scope = rememberCoroutineScope()
    _tabCurrentStatus.value = pagerState.currentPage

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MainBlue,
        contentColor = Color.Gray,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 3.dp,
                color = LightPink,
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = Color.White
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

// TABS SWITCHER CONTROLLER
@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, navController: NavHostController) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> UserListScreen(navController)
            1 -> GroupScreen(navController)
            //2 -> CallListScreen(navController)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MessengerAppDemoTheme() {
        MainAppActivity(navController = NavHostController(LocalContext.current))
    }
}

