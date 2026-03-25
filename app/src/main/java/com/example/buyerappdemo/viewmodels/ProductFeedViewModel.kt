package com.example.buyerappdemo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.buyerappdemo.models.ProductModel
import com.example.buyerappdemo.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

data class ProductFeedUiState(
    val products: List<ProductModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = "",
    val sortBy: String = "price"
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
}
