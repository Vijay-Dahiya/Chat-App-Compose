package co.shree.krishna.shreekrishna.util

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.shree.krishna.shreekrishna.DestinationScreen
import co.shree.krishna.shreekrishna.viewModel.LCViewModel
import coil.compose.rememberAsyncImagePainter

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpToRoute
        launchSingleTop = true
    }
}

@Composable
fun CommonProgressBar() {
    Row(modifier = Modifier
        .alpha(0.5f)
        .background(color = Color.LightGray)
        .clickable(enabled = false) {}
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
    }
}


@Composable
fun CheckSignedIn(vm: LCViewModel, navController: NavController) {

    val alredySignedIn = remember { mutableStateOf(false) }
    val signIn = vm.signIn.value
    if (signIn && !alredySignedIn.value) {
        alredySignedIn.value = true
        navController.navigate(DestinationScreen.ChatList.route) {
            popUpTo(0)
        }
    }
}


@Composable
fun CommonDivider() {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .alpha(.3f)
            .padding(top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun CommonImage(
    modifier: Modifier = Modifier,
    data: String?,
    contentScale: ContentScale = ContentScale.Crop
) {

    val painter = rememberAsyncImagePainter(model = data)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier.wrapContentSize(),
        contentScale = contentScale
    )

}

@Composable
fun TitleText(txt: String) {
    Text(
        text = txt,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun CommonRow(imageUrl: String?, name: String?, onItemClick: () -> Unit) {
    Log.d("Dahiya", "CommonRow: $name $imageUrl")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable { onItemClick.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
        Text(
            text = name ?: "----",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )

    }
}
