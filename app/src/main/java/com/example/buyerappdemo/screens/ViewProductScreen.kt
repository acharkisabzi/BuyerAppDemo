package com.example.buyerappdemo.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.buyerappdemo.R
import com.example.buyerappdemo.models.ProductModel


import com.example.buyerappdemo.ui.theme.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProductScreen(navController: NavController, productModel: ProductModel) {

    val imageUri by remember { mutableStateOf<Uri?>(productModel.imageUrl.toUri()) }
    val productName by remember { mutableStateOf(productModel.name) }
    val productPrice by remember { mutableStateOf(productModel.price) }
    val productShopName = productModel.shopName
    val productShopArea = productModel.area

    Scaffold(containerColor = ADAtSurface, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "BuyerAppDemo",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 21.sp,
                    letterSpacing = (-0.5).sp,
                    color = ADAtOnSurface
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.desc_back),
                        tint = ADAtOnSurface
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ADAtSurfaceLowest.copy(alpha = 0.92f)
            )
        )
    }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Editorial Header ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 4.dp)
            ) {
                Text(
                    text = productName,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp,
                    color = ADAtOnSurface
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Image Picker (Instagram-style square) ─────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ADAtSurfaceLow),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = stringResource(R.string.desc_product_photo),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Product Price
                Column(){
                    Text(
                        text = "Price",
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = productPrice.toString(),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(Modifier.height(1.dp))
                Column(){
                    Text(
                        text = "Sold by:",
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = productShopName,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Column(){
                    Text(
                        text = "Area:",
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = productShopArea,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {navController.navigateUp()},
                    colors = ButtonDefaults.buttonColors(containerColor = DSecondary,
                        contentColor = ADAtSurfaceLowest),
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "Go back",
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
