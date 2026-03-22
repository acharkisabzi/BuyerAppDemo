package com.example.buyerappdemo.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: String? = "",
    val name: String = "",
    val area: String = "",
    val email: String = "",
    val phone: String = ""
)