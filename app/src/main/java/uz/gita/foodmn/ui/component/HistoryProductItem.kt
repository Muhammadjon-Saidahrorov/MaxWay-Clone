package uz.gita.foodmn.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import uz.gita.foodmn.R
import uz.gita.foodmn.app.App
import uz.gita.foodmn.util.ConnectInternet

@Composable
fun HistoryProductItem(
    imageUrl: String,
    price: Long,
    title: String,
    _productCount: Int,
    date: String
) {
    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White)
        ) {
            var productCount by remember { mutableStateOf(_productCount) }
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(140.dp)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                SubcomposeAsyncImage(
                    model = if(ConnectInternet(App.context))imageUrl else R.drawable.placeholder,
                    loading = {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.placeholder),
                                contentDescription = "", modifier = Modifier.background(Color.White)
                            )
                        }
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    alignment = Alignment.Center,
                )
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 28.dp, end = 14.dp)
                    .background(Color.White)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 14.dp)
                        .background(Color.White),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = date,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )

                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 14.dp, bottom = 24.dp),
                    Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "$productCount",
                        color = colorResource(id = R.color.black),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )

                    Text(
                        text = "$price sum",
                        color = colorResource(id = R.color.black),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .background(Color.LightGray)
        )
    }
}