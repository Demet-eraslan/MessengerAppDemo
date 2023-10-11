package com.example.messengerappdemo.view

import android.provider.Settings
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.messengerappdemo.R
import com.example.messengerappdemo.ui.theme.*
import com.example.messengerappdemo.viewModel

@Composable
fun Settings(navController: NavHostController?){
    Scaffold( topBar = {
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
                    Column(
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Ayarlar",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            },
            backgroundColor = MainBlue,
            //elevation = 3.dp
        )
    }){
        Surface(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SettingsTopVariable()
                SettingsMiddleVariable()
            }
        }
    }

}

// SHOW SETTINGS FIRST COMPONENT : PROFILE
@Composable
fun SettingsTopVariable(){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(12.dp),
                elevation = 4.dp
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = R.drawable.bites_400x400,
                        builder = {
                            transformations(CircleCropTransformation())
                        },
                    ),
                    modifier = Modifier.size(65.dp),
                    contentDescription = "Profile picture description",
                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "BİTES",
                    fontSize = 15.sp,
                    color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = viewModel.myLocalIP.toString(),
                    fontSize = 14.sp,
                    color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
    }
}

// SHOW SETTINGS SECOND(MIDDLE) COMPONENT : SETTINGS FUNCTION
@Composable
fun SettingsMiddleVariable(){

    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            //.clickable(onClick = { clickAction.invoke() })
            .size(width = 200.dp, height = 365.dp),
        elevation = 5.dp,
        shape = RoundedCornerShape(1.dp),
        backgroundColor = Color.White
    ){
        Column() {
            val checkedState = remember { mutableStateOf(true) }
            Row(
                Modifier
                    .padding(start = 15.dp)
                    .clickable { },
            ) {
                Icon(

                    Icons.Filled.VpnKey,
                    tint = LightPink,
                    contentDescription = "Hesap",
                    modifier = Modifier.padding(start = 25.dp, top = 15.dp, bottom = 5.dp),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Column(
                    modifier = Modifier.padding(15.dp)

                ) {

                    Text(
                        buildAnnotatedString {
                            append("Hesap")
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Light, fontSize = 15.sp, color = Color.Gray)
                            ) {
                                append("Numara değiştir, Hesap sil ... ")
                            }

                        }
                    )
                }
            }
            //Divider(startIndent = 0.dp, thickness = 1.dp, color = LightBlue)
            Row(
                Modifier
                    .padding(start = 15.dp)
                    .clickable { },
            ) {
                Icon(

                    Icons.Filled.ChatBubble,
                    tint = LightPink,
                    contentDescription = "Sohbetler",
                    modifier = Modifier.padding(start = 25.dp, top = 15.dp, bottom = 5.dp),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Column(
                    modifier = Modifier.padding(15.dp)

                ) {

                    Text(
                        buildAnnotatedString {
                            append("Sohbetler")
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Light, fontSize = 15.sp, color = Color.Gray)
                            ) {
                                append("Sohbet Yedeği, Sohbeti Dışa Aktar... ")
                            }

                        }
                    )
                }
            }
            Row(
                Modifier
                    .padding(start = 15.dp)
                    .clickable { },
            ) {
                Icon(

                    Icons.Filled.Notifications,
                    tint = LightPink,
                    contentDescription = "Notifications",
                    modifier = Modifier.padding(start = 25.dp, top = 15.dp, bottom = 5.dp),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Column(
                    modifier = Modifier.padding(15.dp)

                ) {

                    Text(
                        buildAnnotatedString {
                            append("Bildirimler")
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Light, fontSize = 15.sp, color = Color.Gray)
                            ) {
                                append("Mesaj, Grup, Zil Tonu... ")
                            }

                        }
                    )
                }
            }
            Row(
                Modifier
                    .padding(start = 15.dp)
                    .clickable { },
            ) {
                Icon(

                    Icons.Filled.DataUsage,
                    tint = LightPink,
                    contentDescription = "Data usage ",
                    modifier = Modifier.padding(start = 25.dp, top = 15.dp, bottom = 5.dp),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Column(
                    modifier = Modifier.padding(15.dp)

                ) {

                    Text(
                        buildAnnotatedString {
                            append("Veri Depolama ve Kullanımı")
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Light, fontSize = 15.sp, color = Color.Gray)
                            ) {
                                append("İnternet kullanımı, otomatik indirme... ")
                            }

                        }
                    )
                }
            }
            Row(
                Modifier
                    .padding(start = 15.dp)
                    .clickable { },
            ) {
                Icon(

                    Icons.Filled.Help,
                    tint = LightPink,
                    contentDescription = "Help",
                    modifier = Modifier.padding(start = 25.dp, top = 15.dp, bottom = 5.dp),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Column(
                    modifier = Modifier.padding(15.dp)

                ) {

                    Text(
                        buildAnnotatedString {
                            append("Yardım")
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Light, fontSize = 15.sp, color = Color.Gray)
                            ) {
                                append("SSS...")
                            }

                        }
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun SettingsPreview(){
    Settings(null)
}