package uz.gita.foodmn.ui.screen.myorders

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.foodmn.R
import uz.gita.foodmn.ui.component.HistoryProductItem
import uz.gita.foodmn.ui.component.PlaceHolder

object MyOrdersScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {

            val icon = rememberVectorPainter(image = Icons.Outlined.DateRange)
            return remember {
                TabOptions(0u, "My orders", icon)
            }
        }

    @Composable
    override fun Content() {
        val viewModel: MyOrdersContract.ViewModel = getViewModel<MyOrdersViewModel>()
        val uiState = viewModel.collectAsState()
        HistoryScreenContent(uiState, viewModel::onEventDispatcher)
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun HistoryScreenContent(
    uiState: State<MyOrdersContract.UIState>,
    eventDispatcher: (MyOrdersContract.Intent) -> Unit
) {
    Column(Modifier.background(colorResource(id = R.color.grey))) {
        var deleteAllDialogOpeningValue by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White),

            ) {

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "My orders",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))

            if (uiState.value.products.isNotEmpty()) {
                IconButton(
                    onClick = {
                        if (uiState.value.products.isNotEmpty())
                            deleteAllDialogOpeningValue = true
                    },
                    modifier = Modifier
                        .padding(end = 8.dp),
                ) {
                    Image(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }
            }
        }



        if (uiState.value.products.isNotEmpty()) {

            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.grey))
            ) {
                items(uiState.value.products) { product ->

                    HistoryProductItem(
                        imageUrl = product.imgUrl,
                        price = product.price * product.count,
                        title = product.title,
                        _productCount = product.count,
                        date = product.day
                    )
                }
            }
        } else {
            PlaceHolder(image = R.drawable.buyutrma)
        }


        DialogDeleteAll2(openingValue = deleteAllDialogOpeningValue) {
            deleteAllDialogOpeningValue = false
            if (it)
                eventDispatcher.invoke(MyOrdersContract.Intent.DeleteAll)

        }
    }
}

@Composable
fun DialogDeleteAll2(openingValue: Boolean, listener: (Boolean) -> Unit) {
    if (openingValue) {

        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Attention!",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black, modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete your order history?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { listener.invoke(true) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Yes",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { listener.invoke(false) }, colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(
                            id = R.color.grey
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }, containerColor = Color.White
        )
    }
}