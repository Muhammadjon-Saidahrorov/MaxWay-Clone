package uz.gita.foodmn

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.foodmn.util.navigation.NavigationHandler
import uz.gita.foodmn.ui.screen.home.HomeScreen
import uz.gita.foodmn.ui.theme.FoodMNTheme
import uz.gita.foodmn.util.ConnectInternet
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            FoodMNTheme {
                var isConnect by remember { mutableStateOf(ConnectInternet(this)) }

                val systemUiController = rememberSystemUiController()
                // Change the status bar text color to dark
                systemUiController.setStatusBarColor(
                    color = Color.White, // Status bar background color
                    darkIcons = true // Dark text/icons on the status bar
                )

                if (isConnect) {
                    Navigator(screen = HomeScreen()) { navigator ->
                        LaunchedEffect(navigator) {
                            navigationHandler.navigationStack
                                .onEach { it.invoke(navigator) }
                                .launchIn(lifecycleScope)
                        }
                        CurrentScreen()
                    }
                } else {
                    NotNetworkContent {
                        if (ConnectInternet(this)) {
                            isConnect = true
                        } else {
                            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }

}

@Composable
fun NotNetworkContent(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
                .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.internet),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onClick.invoke() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.ink))
            ) {
                Text(
                    text = "Try again",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
