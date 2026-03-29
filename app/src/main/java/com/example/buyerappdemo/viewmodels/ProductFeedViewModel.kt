package com.example.buyerappdemo.viewmodels

import android.content.ClipData
import android.util.Log
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.buyerappdemo.models.ProductModel
import com.example.buyerappdemo.models.UserModel
import com.example.buyerappdemo.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.update

data class ProductFeedUiState(
    val products: List<ProductModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = "",
    val sortBy: String = "price",
    val shop: UserModel = UserModel(),
    val showDialog: Boolean = false
)

class ProductFeedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductFeedUiState())
    val uiState: StateFlow<ProductFeedUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            try {
                val result = supabase.postgrest["products"]
                    .select {
                        order(column = _uiState.value.sortBy, Order.ASCENDING)
                    }
                val products = result.decodeList<ProductModel>()
                _uiState.value = _uiState.value.copy(
                    products = products,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load products"
                )
            }
        }
    }

    fun setSortFilter(sortBy: String) {
        _uiState.value = _uiState.value.copy(sortBy = sortBy)
        loadProducts()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
    fun dialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDialog = show)
    }

    fun getShopInfo(product: ProductModel) {
        viewModelScope.launch{
            try{
                val result = supabase.postgrest["users"]
                    .select {
                        filter { eq("id", product.shopId) }
                    }
                val shop = result.decodeSingle<UserModel>()
                _uiState.update { currentState ->
                    currentState.copy(shop = shop)
                }
            } catch(e: Exception){
                Log.e("ProductFeedViewModel", "getShopInfo: ", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load shop info"
                )

            }
        }
    }

    fun copyToClipboard(label: String, text: String, clipboard: Clipboard){
        viewModelScope.launch{
            try{
                val clipEntry =
                    ClipEntry(ClipData.newPlainText(label, text))
                clipboard.setClipEntry(clipEntry)
            } catch(e: Exception){
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to copy to clipboard"
                )
            }
        }
    }
}
