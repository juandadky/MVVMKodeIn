package com.dadky.mvvmkoin.data.repositories

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dadky.mvvmkoin.data.db.AppDatabase
import com.dadky.mvvmkoin.data.db.entities.Quote
import com.dadky.mvvmkoin.data.network.MyApi
import com.dadky.mvvmkoin.data.network.SafeApiRequest
import com.dadky.mvvmkoin.data.preferences.PreferenceProvider
import com.dadky.mvvmkoin.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

private const val MINIMAL_INTERVAL = 6

class QuotesRepository(private val api: MyApi, private val db:AppDatabase,private val preferences:PreferenceProvider): SafeApiRequest() {

    private val quotes = MutableLiveData<List<Quote>>()

    init {
        quotes.observeForever {
            saveQuotes(it)
        }
    }

    suspend fun getQuotes(): LiveData<List<Quote>>{
        return withContext(Dispatchers.IO){
            fetchQuotes()
            db.getQuoteDao().getQuotes()
        }
    }

    private suspend fun fetchQuotes(){
        val lastSaveAt = preferences.getLastSavedAt()

        if(lastSaveAt != null || isFetchNeeded(lastSaveAt)){
            val response = apiRequest { api.getQuotes() }
            quotes.postValue(response.quotes)
        }
    }

    private fun isFetchNeeded(lastSaveAt: String?): Boolean {
        if(lastSaveAt != null){
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val parse = LocalDateTime.parse(lastSaveAt)
                ChronoUnit.HOURS.between(parse,LocalDateTime.now())> 6
            }else{
                try{
                    val parse = SimpleDateFormat().parse(lastSaveAt)
                    val diff = Calendar.getInstance().timeInMillis - parse.time
                    diff/(60* 60*1000) > 6
                }catch (e:Exception){
                    return true
                }
            }
        }
        return true
    }


    private fun saveQuotes(quotes: List<Quote>){
        Coroutines.io {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                preferences.saveLastSavedAt(LocalDateTime.now().toString())
            }else{
                preferences.saveLastSavedAt(DateFormat.getTimeInstance().format(Date()))
            }
            db.getQuoteDao().saveAllQuotes(quotes)
        }
    }

}