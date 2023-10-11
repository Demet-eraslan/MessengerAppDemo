package com.example.messengerappdemo.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.messengerappdemo.model.GroupModel
import com.example.messengerappdemo.model.UserModel
import com.example.messengerappdemo.ui.theme.MainError
import com.example.messengerappdemo.ui.theme.MainGreen
import com.example.messengerappdemo.viewModel


@Composable
fun GroupScreen(navController: NavHostController?) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(viewModel.mcGroups.value) { g ->
                GroupCard(grp = g) {
                    navController?.navigate("grp_chat/${g.id}")
                }
            }
        }
    }
}

// EACH CHAT BOX IN THE LIST called in UserListScreen
@Composable
fun GroupCard(grp: GroupModel, clickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { clickAction.invoke() })
            .size(width = 200.dp, height = 100.dp),
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White
    ) {
        // COMPONENTS INSIDE THE BOX
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,

            ) {
            ProfileContent(grp)
        }
    }
}

//called in ProfileCard
@Composable
fun ProfileContent(grpMdl: GroupModel) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = grpMdl.multicastIP,
                modifier = Modifier
                    .weight(1.0f),
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (grpMdl.messages.value.lastOrNull() != null) grpMdl.messages.value.lastOrNull()!!.msgDate else "",
                fontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier.padding(end = 5.dp)
            )

        }
        // LastMessage
        Text(
            text = if (grpMdl.messages.value.lastOrNull() != null) grpMdl.messages.value.lastOrNull()!!.msg else "",
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )

    }
}