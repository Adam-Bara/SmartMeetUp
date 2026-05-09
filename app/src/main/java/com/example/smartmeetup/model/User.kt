package com.example.smartmeetup.model

data class User(
    val id: Int,
    val displayName: String,
    val username: String? = null
)