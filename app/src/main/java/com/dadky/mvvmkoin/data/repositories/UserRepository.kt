package com.dadky.mvvmkoin.data.repositories


import com.dadky.mvvmkoin.data.db.AppDatabase
import com.dadky.mvvmkoin.data.db.entities.User
import com.dadky.mvvmkoin.data.network.MyApi
import com.dadky.mvvmkoin.data.network.SafeApiRequest
import com.dadky.mvvmkoin.data.network.responses.AuthResponse
import retrofit2.Response

class UserRepository(private val api:MyApi,private val db:AppDatabase): SafeApiRequest() {

    suspend fun userLogin(email:String,password:String):AuthResponse{
        return apiRequest { api.userLogin(email, password) }
    }

    suspend fun userSignup(name:String,email:String,password:String): AuthResponse{
        return apiRequest { api.userSignup(name, email, password) }
    }

    suspend fun saveUser(user:User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()

}