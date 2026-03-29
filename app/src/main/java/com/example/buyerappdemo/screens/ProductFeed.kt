package com.example.buyerappdemo.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.buyerappdemo.R
import com.example.buyerappdemo.models.ProductModel
import com.example.buyerappdemo.viewmodels.AuthViewModel
import com.example.buyerappdemo.viewmodels.ProductFeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFeedScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val productFeedViewModel: ProductFeedViewModel = viewModel()
    val productFeedUiState by productFeedViewModel.uiState.collectAsState()
    var expanded: Boolean by remember { mutableStateOf(false) }

    Scaffold(containerColor = MaterialTheme.colorScheme.tertiaryContainer) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top App Bar ───────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Subtle sign-out link
                TextButton(
                    modifier = Modifier.weight(0.9f),
                    onClick = {
                        authViewModel.signOut()
                        navController.navigate("login") {
                            popUpTo("productFeed") { inclusive = true }
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(height = 30.dp, width = 70.dp)
                            .clip(shape = RoundedCornerShape(100.dp))
                            .background(MaterialTheme.colorScheme.onErrorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.btn_sign_out),
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

                Text(
                    text = "BuyerAppDemo",
                    modifier = Modifier.weight(1.5f),
                    style =MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.W900,
                    letterSpacing = (-0.5).sp,
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.weight(0.9f),
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options",
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioHighBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                        offset = DpOffset((-20).dp, 0.dp)
                    ) {
                        Text(
                            text = "Sort by",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Price") },
                            onClick = {
                                productFeedViewModel.setSortFilter("price")
                                expanded = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Area") },
                            onClick = {
                                productFeedViewModel.setSortFilter("area")
                                expanded = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Shop name") },
                            onClick = {
                                productFeedViewModel.setSortFilter("shop_name")
                                expanded = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Product name") },
                            onClick = {
                                productFeedViewModel.setSortFilter("name")
                                expanded = false
                            },
                        )
                    }
                }
            }
            // ── Collection Grid ───────────────────────────────────────────────
            CollectionSection(
                products = productFeedUiState.products,
                isLoading = productFeedUiState.isLoading,
                navController = navController
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CollectionSection(
    products: List<ProductModel>,
    isLoading: Boolean,
    navController: NavController
) {
    // Section header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Available products",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.3).sp
        )
        Text(
            text = "${products.size} items",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        products.isEmpty() -> {
            // Empty state — editorial style
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(vertical = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.msg_no_products),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.products_will_be_available_soon),
                        fontSize = 14.sp
                    )
                }
            }
        }

        else -> {
            // 2-column grid — rendered in a fixed-height column since we're inside verticalScroll
            // We use a chunked list to avoid nested scroll conflicts
            val rows = products.chunked(2)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rows.forEach { rowProducts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            AtelierProductCard(
                                product = product,
                                modifier = Modifier.weight(1f),
                                navController = navController
                            )
                        }
                        // Fill remaining space if odd number in last row
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// ─── Product Card ─────────────────────────────────────────────────────────────
@SuppressLint("RememberInComposition")
@Composable
private fun AtelierProductCard(
    product: ProductModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = true,
                onClickLabel = "Edit ${product.productName}",
                onClick = { navController.navigate(product) }
            ),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        // Product image — 1:1 aspect ratio
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.productName,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentScale = ContentScale.Crop
        )

        // Product info
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = product.productName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.price_format, product.price.toInt()),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xF88668F6)
                )
                // Stock badge — tonal, no hard border
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            if (product.inStock)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                Color(0xFFFFE0E0)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (product.inStock)
                            stringResource(R.string.status_in_stock)
                        else
                            stringResource(R.string.status_out_stock),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (product.inStock) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    }
}
