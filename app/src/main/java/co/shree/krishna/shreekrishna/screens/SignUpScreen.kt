package co.shree.krishna.shreekrishna.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.shree.krishna.shreekrishna.DestinationScreen
import co.shree.krishna.shreekrishna.R
import co.shree.krishna.shreekrishna.util.CheckSignedIn
import co.shree.krishna.shreekrishna.util.CommonProgressBar
import co.shree.krishna.shreekrishna.util.navigateTo
import co.shree.krishna.shreekrishna.viewModel.LCViewModel

@Composable
fun SignUpScreen(navController: NavController, vm: LCViewModel) {

    CheckSignedIn(vm = vm, navController = navController)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val nameState = remember {
                mutableStateOf(TextFieldValue())
            }
            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val numberState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }
            val focus = LocalFocusManager.current
            Image(
                painter = painterResource(id = R.drawable.social),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )

            Text(
                text = "Sign Up", fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = nameState.value,
                onValueChange = { newValue ->
                    nameState.value = newValue
                },
                label = { Text(text = "Name") },
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = numberState.value,
                onValueChange = { newValue ->
                    numberState.value = newValue
                },
                label = { Text(text = "Number") },
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { newValue ->
                    emailState.value = newValue
                },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { newValue ->
                    passwordState.value = newValue
                },
                label = { Text(text = "Password") },
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = {
                    vm.signUp(
                        nameState.value.text,
                        numberState.value.text,
                        emailState.value.text,
                        passwordState.value.text
                    )
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Sign Up")
            }

            Text(text = "Already a user? Go to login -> ",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.Login.route)
                    }
            )
        }
    }

    if (vm.isProgress.value) CommonProgressBar()
}