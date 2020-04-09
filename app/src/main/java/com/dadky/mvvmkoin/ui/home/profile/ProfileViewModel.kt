package com.dadky.mvvmkoin.ui.home.profile

import androidx.lifecycle.ViewModel
import com.dadky.mvvmkoin.data.repositories.UserRepository

class ProfileViewModel(repository: UserRepository) : ViewModel() {

    val user = repository.getUser()
}
