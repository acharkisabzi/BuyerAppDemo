package com.example.buyerappdemo.models

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: String? = "",
    val username: String = "",
    val name: String = "",
    val area: String = "",
    val email: String = "",
    val phone: String = ""
)