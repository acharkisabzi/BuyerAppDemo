package com.example.buyerappdemo.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopModel(
    val id: String? = "",
    @SerialName("shop_name") val shopName: String = "",
    val area: String = "",
    val role: String = "shop",
    val phone: String = "",
    val email: String = ""
)
