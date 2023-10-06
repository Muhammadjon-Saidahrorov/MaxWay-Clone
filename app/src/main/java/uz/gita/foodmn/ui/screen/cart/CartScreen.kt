package uz.gita.foodmn.ui.screen.cart

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.foodmn.R
import uz.gita.foodmn.data.model.OrderedProductData
import uz.gita.foodmn.ui.component.OrderedProductItem
import uz.gita.foodmn.ui.component.PlaceHolder
import uz.gita.foodmn.ui.component.RecommendProductItem
import uz.gita.foodmn.util.logger
import uz.gita.foodmn.util.myToast
import java.text.SimpleDateFormat
import java.util.*


object CartScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(image = Icons.Outlined.ShoppingCart)

            return remember {
                TabOptions(0u, "Cart", icon)
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: CartContract.ViewModel = getViewModel<CartViewModel>()
        val uiState = viewModel.collectAsState()

        viewModel.collectSideEffect {
            when (it) {
                is CartContract.SideEffect.Toast -> {
                    myToast(it.m, context)
                }
            }
        }

        var productPriceAll = mutableStateOf(0L)
        var size by remember { mutableStateOf(0) }

        var deleteAllDialogOpeningValue by remember { mutableStateOf(false) }
        var orderedDialogOpeningValue by remember { mutableStateOf(false) }
        var logicTest by remember { mutableStateOf(false) }

        Log.d("QQQ", "9")

        Scaffold(topBar = {
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
                    text = "Cart",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                if (logicTest) {
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
        },
            content = {
                BucketScreenContent(
                    uiState = uiState,
                    eventDispatcher = viewModel::onEventDispatcher,
                    deleteAllDialogOpeningValue,
                    orderedDialogOpeningValue,
                    it,
                    {
                        deleteAllDialogOpeningValue = false
                        if (it)
                            viewModel::onEventDispatcher.invoke(
                                CartContract.Intent.DeleteAll(
                                    uiState.value.products
                                )
                            )
                    },
                    {
                        orderedDialogOpeningValue = false
                        if (it)
                            viewModel::onEventDispatcher.invoke(CartContract.Intent.Order(uiState.value.products))
                    },
                    productPriceAll
                )
            },
            bottomBar = {
                if (uiState.value.products.isNotEmpty()) {
                    logicTest = true
                    size = uiState.value.products.size

                    Column(modifier = Modifier.background(Color.White)) {
                        Row(Modifier.padding(horizontal = 32.dp, vertical = 16.dp)) {
                            Text(
                                text = "Order price",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "${productPriceAll.value} sum",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Button(
                            onClick = {
                                if (uiState.value.products.isNotEmpty())
                                    orderedDialogOpeningValue = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.ink))
                        ) {
                            Text(
                                text = "Checkout order",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    logicTest = false
                }
            })

    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun BucketScreenContent(
    uiState: State<CartContract.UIState>,
    eventDispatcher: (CartContract.Intent) -> Unit,
    deleteAllDialogValue: Boolean,
    orderedDialogValue: Boolean,
    padding: PaddingValues,
    deleteAllOnClick: (Boolean) -> Unit,
    orderedOnClick: (Boolean) -> Unit,
    productPriceAll: MutableState<Long>
) {

    var dialogOpeningValue by remember { mutableStateOf(false) }
    var deleteAllDialogOpeningValue by remember { mutableStateOf(deleteAllDialogValue) }
    deleteAllDialogOpeningValue = deleteAllDialogValue
    var orderedDialogOpeningValue by remember { mutableStateOf(orderedDialogValue) }
    orderedDialogOpeningValue = orderedDialogValue
    var selectedData by remember {
        mutableStateOf(OrderedProductData(1, "", 0L, "", 0, false, ""))
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .background(colorResource(id = R.color.grey))
    ) {


        if (uiState.value.products.isNotEmpty()) {

            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
            )

            MyDialog(openingValue = dialogOpeningValue) {
                dialogOpeningValue = false
                if (it)
                    eventDispatcher.invoke(CartContract.Intent.DeleteData(selectedData))
            }

            DialogDeleteAll(openingValue = deleteAllDialogOpeningValue) {
                deleteAllOnClick.invoke(it)
            }

            OrderedDialog(openingValue = orderedDialogOpeningValue, listener = {
                orderedOnClick.invoke(it)
            })

            var price = 0L
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                var i = 0
                items(uiState.value.products) { product ->
                    var productCount by remember { mutableStateOf(product.count) }

                    price+=product.price * productCount
                    i++
                    if (i==uiState.value.products.size){
                        productPriceAll.value=price
                    }

                    OrderedProductItem(
                        imageUrl = product.imgUrl,
                        price = product.price * productCount,
                        title = product.title,
                        _productCount = productCount,
                        onClickDelete = {
                            dialogOpeningValue = true
                            selectedData = product
                        },
                        clickCount = {
                            productCount = it
                            eventDispatcher.invoke(CartContract.Intent.Update(product.copy(count = productCount)))
                        }
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(
                                colorResource(
                                    id = R.color.grey
                                )
                            )
                    )
                    Column(modifier = Modifier.background(Color.White)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Recommended",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow {
                            items(uiState.value.recommendProducts) { product ->
                                RecommendProductItem(data = product) {
                                    eventDispatcher.invoke(
                                        CartContract.Intent.Add(
                                            OrderedProductData(
                                                0,
                                                product.imgUrl,
                                                product.price,
                                                product.title,
                                                1,
                                                false,
                                                getDate()
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

            }
        } else {
            PlaceHolder(image = R.drawable.savatcha)

        }

    }
}

@Composable
fun MyDialog(openingValue: Boolean, listener: (Boolean) -> Unit) {
    if (openingValue) {

        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(
                    text = "Attention!",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black, modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Are you sure?\nDo you want to remove a product from the list?",
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
                    onClick = { listener.invoke(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                        colorResource(
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

@Composable
fun DialogDeleteAll(openingValue: Boolean, listener: (Boolean) -> Unit) {
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
                    text = "Are you sure you want to clear cart?",
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

@Composable
fun OrderedDialog(openingValue: Boolean, listener: (Boolean) -> Unit) {
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
                    text = "Send orders?",
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

private fun getDate(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val currentDate = Date()
    val formattedDate = dateFormat.format(currentDate)
    return formattedDate
}


