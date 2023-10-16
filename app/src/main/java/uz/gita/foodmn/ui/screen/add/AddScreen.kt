package uz.gita.foodmn.ui.screen.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import coil.compose.SubcomposeAsyncImage
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.foodmn.R
import uz.gita.foodmn.app.App
import uz.gita.foodmn.data.model.OrderedProductData
import uz.gita.foodmn.data.model.ProductData
import uz.gita.foodmn.util.navigation.AppScreen
import uz.gita.foodmn.util.ConnectInternet
import uz.gita.foodmn.util.myToast
import java.text.SimpleDateFormat
import java.util.*

class AddScreen(private val data: ProductData) : AppScreen() {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: AddContract.ViewModel = getViewModel<AddViewModel>()
        val uiState = viewModel.collectAsState()

        viewModel.collectSideEffect {
            when (it) {
                is AddContract.SideEffect.Toast -> {
                    myToast(it.message, context)
                }
            }
        }

        AddScreenContent(
            data = data,
            eventDispatcher = viewModel::onEventDispatcher,
            uiState = uiState
        )
    }
}

@Composable
fun AddScreenContent(
    data: ProductData,
    uiState: State<AddContract.UIState>,
    eventDispatcher: (AddContract.Intent) -> Unit
) {
    var productCount by remember { mutableStateOf(1) }

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {

            SubcomposeAsyncImage(
                model = if (ConnectInternet(App.context)) data.imgUrl else R.drawable.placeholder,
                loading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(38.dp),
                            color = colorResource(
                                id = R.color.ink
                            ),
                            strokeWidth = 1.5.dp
                        )
                    }
                },
                contentScale = ContentScale.Crop,
                contentDescription = "",
                alignment = Alignment.Center,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = { eventDispatcher.invoke(AddContract.Intent.Back) }, modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {

                Image(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    )

            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = data.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = data.info,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

        }

        Spacer(modifier = Modifier.weight(1f))


        Row(
            Modifier
                .fillMaxWidth()
                .height(76.dp)
                .background(Color.White)
                .padding(horizontal = 10.dp), Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            Row(
                Modifier
                    .height(46.dp)
                    .width(130.dp)
                    .border(
                        1.dp,
                        color = colorResource(id = R.color.grey),
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spacer(modifier = Modifier.width(10.dp))
                TextButton(
                    onClick = { --productCount }, enabled = if (productCount < 2) false else true
                ) {

                    Text(
                        text = "—",
                        fontSize = 16.sp,
                        color = if (productCount == 1) Color.LightGray else Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = productCount.toString(),
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = { ++productCount }, modifier = Modifier
                        .padding(start = 12.dp, end = 10.dp)
                ) {
                    Text(text = "+", fontSize = 16.sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.width(10.dp))
            }

            Text(
                text = "${data.price * productCount} sum",
                color = colorResource(id = R.color.black),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(end = 10.dp)
            )
        }

        Column(
            Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(66.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    eventDispatcher.invoke(
                        AddContract.Intent.Add(
                            OrderedProductData(
                                0, data.imgUrl, data.price, data.title, productCount, false,
                                getDate()
                            )
                        )
                    )
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(
                        id = R.color.ink
                    )
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Add",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }

    }
}

private fun getDate(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val currentDate = Date()
    val formattedDate = dateFormat.format(currentDate)
    return formattedDate
}