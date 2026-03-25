package com.example.buyerappdemo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.buyerappdemo.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.buyerappdemo.R
import com.example.buyerappdemo.models.ProductModel
import com.example.buyerappdemo.supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFeedScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var products by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Load products when screen opens
    LaunchedEffect(Unit) {
        try {
            val result = supabase.postgrest["products"]
                .select {
                    order("price", Order.ASCENDING)
                }
            products = result.decodeList<ProductModel>()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isLoading = false
    }

    Scaffold(
        containerColor = DSurface,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DSurface)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top App Bar ───────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BuyerAppDemo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DOnSurface,
                    letterSpacing = (-0.5).sp
                )
                // Subtle sign-out link
                TextButton(
                    onClick = {
                        scope.launch {
                            supabase.auth.signOut()
                            navController.navigate("login") {
                                popUpTo("feed") { inclusive = true }
                            }
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_sign_out),
                        color = DOutline,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            // ── Collection Grid ───────────────────────────────────────────────
            CollectionSection(
                products = products,
                isLoading = isLoading,
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
            color = DOnSurface,
            letterSpacing = (-0.3).sp
        )
        Text(
            text = "${products.size} items",
            fontSize = 13.sp,
            color = DOutline,
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
                    color = DSecondary,
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
                    .background(DSurfaceContainerLow)
                    .padding(vertical = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.msg_no_products),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DOnSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.products_will_be_available_soon),
                        fontSize = 14.sp,
                        color = DOnSurfaceVariant
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
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DSurfaceContainerLowest)
            // Ambient shadow — feels architectural, not stamped
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = DOnSurface.copy(alpha = 0.04f),
                spotColor = DOnSurface.copy(alpha = 0.04f)
            )
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                enabled = true,
                onClickLabel = "Edit ${product.name}",
                onClick = { navController.navigate(product) }
            ),
    ) {
        // Product image — 1:1 aspect ratio
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentScale = ContentScale.Crop
        )

        // Product info
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = DOnSurface,
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DSecondary
                )
                // Stock badge — tonal, no hard border
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            if (product.inStock)
                                DSecondaryContainer
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
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (product.inStock) DSecondary else DError,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    }
}
