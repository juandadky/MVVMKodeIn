package com.dadky.mvvmkoin.ui.auth

import androidx.lifecycle.LiveData
import com.dadky.mvvmkoin.data.db.entities.User

interface AuthListener {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(msgError:String)
}