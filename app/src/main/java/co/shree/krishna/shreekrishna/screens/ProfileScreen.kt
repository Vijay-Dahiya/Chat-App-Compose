package co.shree.krishna.shreekrishna.screens

import android.bluetooth.BluetoothAssignedNumbers
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.shree.krishna.shreekrishna.DestinationScreen
import co.shree.krishna.shreekrishna.util.CommonDivider
import co.shree.krishna.shreekrishna.util.CommonImage
import co.shree.krishna.shreekrishna.util.CommonProgressBar
import co.shree.krishna.shreekrishna.util.navigateTo
import co.shree.krishna.shreekrishna.viewModel.LCViewModel

@Composable
fun ProfileScreen(vm: LCViewModel, navController: NavController) {
    val isProcess = vm.isProgress.value
    if (isProcess) {
        CommonProgressBar()
    } else {
        Log.d("Vijay", "ProfileScreen: ${vm.userData.value}")
        val userData = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name?:"")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number?: "")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
        ) {
            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                name = name,
                number = number,
                vm = vm,
                onBack = {
                    navigateTo(navController,DestinationScreen.ChatList .route)
                },
                onSave = {
                         vm.createOrUpdateProfile(
                             name= name,
                             number = number
                         )
                },
                onNameChange = { name=it },
                onNumberChange = { number = it },
                onLogOut = {
                    vm.logOut()
                    navigateTo(navController,DestinationScreen.Login.route)
                }
            )
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController
            )
        }
    }

}

@Composable
fun ProfileContent(
    modifier: Modifier,
    name: String,
    number: String,
    vm: LCViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onLogOut: () -> Unit
) {
    val imageUrl = vm.userData.value?.imageUrl
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", modifier = Modifier.clickable {
                onBack.invoke()
            })
            Text(text = "Save", modifier = Modifier.clickable {
                onSave.invoke()
            })
        }
        CommonDivider()
        ProfileImage(imageUrl = imageUrl, vm = vm)
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name, onValueChange = onNameChange, colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Number", modifier = Modifier.width(100.dp))
            TextField(
                value = number, onValueChange = onNumberChange, colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.Center
        ) {

            Text(text = "Logout", modifier = Modifier.clickable { onLogOut.invoke() })

        }

    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                }, horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                CommonImage(data = imageUrl)
                Text(text = "Change profile image")
            }

            if (vm.isProgress.value) CommonProgressBar()

        }
    }
}