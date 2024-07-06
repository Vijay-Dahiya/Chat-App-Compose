 package co.shree.krishna.shreekrishna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.shree.krishna.shreekrishna.screens.ChatListScreen
import co.shree.krishna.shreekrishna.screens.LoginScreen
import co.shree.krishna.shreekrishna.screens.ProfileScreen
import co.shree.krishna.shreekrishna.screens.SignUpScreen
import co.shree.krishna.shreekrishna.screens.SingleChatScreen
import co.shree.krishna.shreekrishna.screens.StatusScreen
import co.shree.krishna.shreekrishna.ui.theme.ShreeKrishnaTheme
import co.shree.krishna.shreekrishna.viewModel.LCViewModel
import dagger.hilt.android.AndroidEntryPoint

 sealed class DestinationScreen(var route : String) {
     object SignUp : DestinationScreen("signup")
     object Login : DestinationScreen("login")
     object Profile : DestinationScreen("profile")
     object ChatList : DestinationScreen("chatList")
     object SingleChat : DestinationScreen("singleChat/{chatId}") {
         fun createRoute(id : String) = "singleChat/$id"
     }
     object StatusList : DestinationScreen("statusList")
     object SingleStatus : DestinationScreen("singleStatus/{statusId}") {
         fun createRoute(id : String) = "singleStatus/$id"
     }
 }
 @AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShreeKrishnaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigation() {
        val navController = rememberNavController()

        val viewModel = hiltViewModel<LCViewModel>()
        NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route) {
            composable(DestinationScreen.SignUp.route) {
                SignUpScreen(navController, viewModel)
            }

            composable(DestinationScreen.Login.route) {
                LoginScreen(navController = navController, vm = viewModel)
            }

            composable(DestinationScreen.ChatList.route) {
                ChatListScreen(vm = viewModel, navController = navController)
            }

            composable(DestinationScreen.SingleChat.route) {
                val chatId = it.arguments?.getString("chatId")
                chatId?.let {
                    SingleChatScreen(vm = viewModel, navController = navController,chatId)
                }
            }

            composable(DestinationScreen.StatusList.route) {
                StatusScreen(vm = viewModel, navController = navController)
            }

            composable(DestinationScreen.Profile.route) {
                ProfileScreen(vm = viewModel, navController = navController)
            }

        }
    }
}

