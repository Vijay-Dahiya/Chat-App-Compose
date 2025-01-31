package co.shree.krishna.shreekrishna.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.shree.krishna.shreekrishna.DestinationScreen
import co.shree.krishna.shreekrishna.util.CommonProgressBar
import co.shree.krishna.shreekrishna.util.CommonRow
import co.shree.krishna.shreekrishna.util.TitleText
import co.shree.krishna.shreekrishna.util.navigateTo
import co.shree.krishna.shreekrishna.viewModel.LCViewModel

@Composable
fun ChatListScreen(vm: LCViewModel, navController: NavController) {


    if (vm.inProgressChat.value) {
        CommonProgressBar()
        return
    }

    val chats = vm.chats.value
    val userData = vm.userData.value
    val showDialog = remember {
        mutableStateOf(false)
    }
    val onFabClick:() ->Unit = {showDialog.value = true}
    val onDismiss:() ->Unit = {showDialog.value = false}
    val onAddChat:(String) ->Unit = {
        vm.onAddChat(it)
        showDialog.value = false
    }

    Scaffold(
        floatingActionButton = {
            Fab(
                showDialog = showDialog.value,
                onFabClick = onFabClick,
                onDismiss = onDismiss,
                onAddChat = onAddChat
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                TitleText(txt = "Chats")
                if (chats.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No Chats Available")
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1F)){
                        items(chats) {chat->
                            val chatUser = if (chat.user1.userId == userData?.userId) {
                                chat.user2
                            } else {
                                chat.user1
                            }
                            Log.d("Vijay", "ChatListScreen: ${chatUser.userId}")
                            chatUser.userId?.let{
                                CommonRow(imageUrl = chatUser.imageUrl, name = chatUser.name) {
                                    navigateTo(navController = navController, route = DestinationScreen.SingleChat.createRoute(id = it))
                                }
                            }

                        }
                    }
                }
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.CHATLIST,
                    navController = navController
                )
            }
        }
    )

}

@Composable
fun Fab(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss.invoke()
                addChatNumber.value = ""
            },
            confirmButton = {
                Button(onClick = { onAddChat(addChatNumber.value) }) {
                    Text(text = "Add Chat")
                }
            },
            title = { Text(text = "Add Chat") },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value,
                    onValueChange = {addChatNumber.value = it},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )
        return
    }

    FloatingActionButton(
        onClick = {onFabClick.invoke()},
        backgroundColor = MaterialTheme.colors.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
    }
}