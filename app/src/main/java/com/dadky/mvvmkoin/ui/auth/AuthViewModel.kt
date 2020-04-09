package com.dadky.mvvmkoin.ui.auth

import android.content.Intent
import android.view.View
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.dadky.mvvmkoin.data.repositories.UserRepository
import com.dadky.mvvmkoin.util.ApiException
import com.dadky.mvvmkoin.util.Coroutines
import com.dadky.mvvmkoin.util.NoInternetException

class AuthViewModel(private val repository: UserRepository): ViewModel() {
    var email:String?= null
    var password:String?=null
    var name:String?=null
    var passwordConfirm:String?=null
    var authListener:AuthListener?=null

    fun getLoggedInUser() = repository.getUser()

    fun onLoginButtonClick(view:View){
        authListener?.onStarted()
        if(email.isNullOrEmpty()){
            authListener?.onFailure("Ingrese su email")
            return
        }
        if(password.isNullOrEmpty()){
            authListener?.onFailure("Ingrese su contraseña")
            return
        }
        if(isEmailNotValid(email!!)){
            authListener?.onFailure("El email ingresado no es válido")
            return
        }

        Coroutines.main {
            try {
                val authResponse = repository.userLogin(email!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse?.message!!)
            }catch (e:ApiException){
                authListener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                authListener?.onFailure(e.message!!)
            }
        }
    }

    fun onLogin(view:View){
        Intent(view.context,SignUpActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            view.context.startActivity(it)
        }
    }

    fun onSignUp(view:View){
        Intent(view.context,SignUpActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun onSignupButtonClick(view:View){
        authListener?.onStarted()
        if(name.isNullOrEmpty()){
            authListener?.onFailure("Ingrese su nombre")
            return
        }
        if(email.isNullOrEmpty()){
            authListener?.onFailure("Email es necesario")
            return
        }
        if(isEmailNotValid(email!!)){
            authListener?.onFailure("El email no es válido")
            return
        }
        if(password.isNullOrEmpty()){
            authListener?.onFailure("Ingrese una contraseña")
            return
        }
        if(passwordConfirm.isNullOrEmpty()){
            authListener?.onFailure("Ingrese una contraseña")
            return
        }

        if(passwordConfirm != password){
            authListener?.onFailure("Las contraseñas no son iguales")
            return
        }

        Coroutines.main {
            try {
                val authResponse = repository.userSignup(name!!,email!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse?.message!!)
            }catch (e:ApiException){
                authListener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                authListener?.onFailure(e.message!!)
            }
        }
    }

    private fun isEmailNotValid(email:String):Boolean{
        return !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }
}