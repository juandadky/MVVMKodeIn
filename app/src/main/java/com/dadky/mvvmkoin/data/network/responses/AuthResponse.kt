package com.dadky.mvvmkoin.data.network.responses

import com.dadky.mvvmkoin.data.db.entities.User

data class AuthResponse(
    val isSuccessful: Boolean?,
    val message:String?,
    val user: User?
)