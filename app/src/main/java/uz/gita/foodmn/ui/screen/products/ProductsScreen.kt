package uz.gita.foodmn.ui.screen.products

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.foodmn.R
import uz.gita.foodmn.ui.component.*
import uz.gita.foodmn.util.logger
import uz.gita.foodmn.util.myToast


object ProductsScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(image = Icons.Outlined.Home)

            return remember {
                TabOptions(0u, "Home", icon)
            }
        }

    @Composable
    override fun Content() {
        val context = LocalContext.current

        val viewModel: ProductContract.ViewModel = getViewModel<ProductViewModel>()
        val uiState = viewModel.collectAsState()

        viewModel.collectSideEffect {
            when (it) {
                is ProductContract.SideEffect.Toast -> {
                    myToast(it.message, context)
                }
            }
        }

        ProductScreenContent(uiState, viewModel::onEventDispatcher)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductScreenContent(
    uiState: State<ProductContract.UIState>,
    eventDispatcher: (ProductContract.Intent) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(uiState.value.lastSelectedCategory) }
    selectedCategory = uiState.value.lastSelectedCategory

    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey))
    ) {


        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.grey))
        ) {

            Column(Modifier.background(Color.White)) {
                Text(
                    text = "Delivery",
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 16.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    Modifier.padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.place),
                        contentDescription = "Location",
                        Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "O'zbekistan, Tashkent, Chilonzor tumani, Lutfiy ko'chasi 42 dom 86 xonadon",
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.down),
                            contentDescription = null,
                            Modifier.size(16.dp)
                        )
                    }
                }
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp),
                    value = searchText,
                    onValueChange = {
                        if (it.length >= 3) {
                            eventDispatcher.invoke(ProductContract.Intent.Search(it))
                        } else if (searchText.length >= 3) {
                            eventDispatcher.invoke(ProductContract.Intent.AllProduct)
                        }
                        searchText = it
                    },
                    placeholder = {
                        Text(
                            text = "Search",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = colorResource(id = R.color.grey),
                        cursorColor = colorResource(id = R.color.ink)
                    ),
                    singleLine = false,
                    shape = RoundedCornerShape(14.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.glass),
                            contentDescription = null,
                            Modifier.size(24.dp),
                            tint = Color(0xFF858585)
                        )
                    },
                    trailingIcon = {
                        if (searchText.length >= 3) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel),
                                    contentDescription = null,
                                    Modifier.size(16.dp),
                                    tint = Color(0xFF858585)
                                )
                            }
                        }
                    })
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (searchText.trim().length < 3 || searchText.isBlank()) {

                if (!uiState.value.loading) {

//                    val logic = remember {
//                        mutableStateOf(true)
//                    }

                    LazyRow(Modifier.background(Color.White)) {
                        items(uiState.value.categories) { category ->
                            var textColor: Int
                            var backgroundColor: Int

                            if (category == selectedCategory/* && logic.value*/) {
                                textColor = R.color.white
                                backgroundColor = R.color.ink
                            } else {
                                textColor = R.color.black
                                backgroundColor = R.color.grey
                            }

                            CategoryItem(
                                name = category,
                                textColor = textColor,
                                backgrounColor = backgroundColor
                            ) {
//                                if (backgroundColor == R.color.ink) {
//                                    textColor = R.color.black
//                                    backgroundColor = R.color.grey
//                                    logic.value = false
//                                    eventDispatcher.invoke(ProductContract.Intent.AllProduct)
//                                } else {
//                                    logic.value = true
//                                    textColor = R.color.white
//                                    backgroundColor = R.color.ink
//                                    eventDispatcher.invoke(ProductContract.Intent.Category(category))
//                                }
                                eventDispatcher.invoke(ProductContract.Intent.Category(category))
                            }

                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                    LazyColumn {
                        item {
                            ImageSlider()
                        }

                        uiState.value.productsWithCategory.forEach { productData2 ->
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                CategoryItemBig(name = productData2.categoryName)
                            }

                            items(productData2.products) { product ->
                                ProductItem(
                                    imageUrl = product.imgUrl,
                                    info = product.info,
                                    price = product.price,
                                    title = product.title
                                ) {
                                    eventDispatcher.invoke(ProductContract.Intent.AddScreen(product))
                                }
                            }
                        }


                    }
                }
            } else {
                LazyColumn(Modifier.background(colorResource(id = R.color.grey))) {
                    items(uiState.value.searchedProducts) { product ->
                        SearchedProductItem(
                            imageUrl = product.imgUrl,
                            title = product.title,
                            price = product.price
                        ) {
                            eventDispatcher.invoke(ProductContract.Intent.AddScreen(product))
                        }
                    }
                }
            }
        }

        if (uiState.value.loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(60.dp)
                    .padding(top = 40.dp),
                color = colorResource(
                    id = R.color.ink
                )
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider() {


    val pageState = rememberPagerState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(210.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
        ) {

            HorizontalPager(
                pageCount = 3,
                state = pageState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                when (it) {
                    0 -> {
                        Card(Modifier.padding(12.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.slide_1),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    1 -> {
                        Card(Modifier.padding(12.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.slide_2),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    2 -> {
                        Card(Modifier.padding(12.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.slide_3),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            PageIndicator(
                3,
                pageState.currentPage,
                Color(0xFF51277D),
                Color(0xFFE1E1E2),
                8.dp,
                16.dp,
                4.dp,
                modifier = Modifier
                    .wrapContentSize(),
            )

        }
    }
}