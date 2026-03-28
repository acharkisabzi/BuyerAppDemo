package com.example.buyerappdemo.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.buyerappdemo.R
import com.example.buyerappdemo.models.ProductModel
import com.example.buyerappdemo.viewmodels.ProductFeedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProductScreen(
    navController: NavController,
    productModel: ProductModel,
    viewModel: ProductFeedViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState()
    val clipboard = LocalClipboard.current
    val imageUri by remember { mutableStateOf<Uri?>(productModel.imageUrl.toUri()) }
    val productName by remember { mutableStateOf(productModel.name) }
    val productPrice by remember { mutableStateOf(productModel.price) }
    val productShopName = productModel.shopName
    val productShopArea = productModel.area

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Product Details",
                    fontWeight = FontWeight.W900,
                    style = MaterialTheme.typography.titleLarge,
                    letterSpacing = (-0.5).sp
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.desc_back),
                    )
                }
            },
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(
                modifier = Modifier.height(28.dp)
            )
            // ── Image Viewer ─────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = stringResource(R.string.desc_product_photo),
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = true,
                            onClick = {
                                navController.navigate(
                                    "ImageViewScreen/${
                                        Uri.encode(
                                            productModel.imageUrl
                                        )
                                    }"
                                )
                            }),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(5.dp))

            // ── Editorial Header ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 4.dp)
            ) {
                Text(
                    text = productName,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.W900,
                    letterSpacing = (-0.5).sp,
                )
            }

            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Product Price
                Row {
                    Text(
                        text = "Price: "+"₹" + productPrice.toInt().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row {
                    Text(
                        text = "Sold by: ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = productShopName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row {
                    Text(
                        text = "Area: $productShopArea",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.dialog(!state.value.showDialog) },
                    modifier = Modifier.fillMaxWidth()
                ) {

                        Text(
                            text = "Contact shop",
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Contact shop"
                        )
                    if (state.value.showDialog) {
                        viewModel.getShopInfo(productModel)
                        ModalBottomSheet(onDismissRequest = { viewModel.dialog(false) },
                            ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(16.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {

                                Row(
                                    modifier = Modifier.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        enabled = true,
                                        onClick = {
                                            viewModel.copyToClipboard("Phone number", state.value.shop.phone, clipboard)
                                        }
                                    )
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "Phone number",
                                        modifier = Modifier.padding(5.dp),
                                    )
                                    Text(
                                        text = state.value.shop.phone,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                                Row(
                                    modifier = Modifier.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        enabled = true,
                                        onClick = {
                                            viewModel.copyToClipboard("Email ID", state.value.shop.email, clipboard)
                                        }
                                    )
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MailOutline,
                                        contentDescription = "Email i d",
                                        modifier = Modifier.padding(5.dp),
                                    )
                                    Text(
                                        text = state.value.shop.email,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}