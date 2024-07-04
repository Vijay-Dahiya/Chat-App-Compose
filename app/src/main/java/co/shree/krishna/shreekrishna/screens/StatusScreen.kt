package co.shree.krishna.shreekrishna.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import co.shree.krishna.shreekrishna.viewModel.LCViewModel

@Composable
fun StatusScreen(vm : LCViewModel, navController: NavController) {
    BottomNavigationMenu(
        selectedItem = BottomNavigationItem.STATUSLIST,
        navController = navController
    )
}