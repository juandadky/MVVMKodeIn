package com.dadky.mvvmkoin.ui.home.quotes

import androidx.lifecycle.ViewModel
import com.dadky.mvvmkoin.data.repositories.QuotesRepository
import com.dadky.mvvmkoin.util.lazyDeferred

class QuotesViewModel(repository: QuotesRepository) : ViewModel() {

    val quotes by lazyDeferred {
        repository.getQuotes()
    }
}
