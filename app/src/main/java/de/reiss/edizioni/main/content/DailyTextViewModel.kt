package de.reiss.edizioni.main.content

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.architecture.AsyncLoadStatus
import de.reiss.edizioni.model.DailyText
import java.util.*

open class DailyTextViewModel(private val repository: DailyTextRepository) : ViewModel() {

    private val dailyTextLiveData: MutableLiveData<AsyncLoad<DailyText>> = MutableLiveData()

    open fun dailyTextLiveData() = dailyTextLiveData

    open fun loadDailyText(date: Date) {
        repository.getDailyTextFor(date, dailyTextLiveData())
    }

    fun dailyText() = dailyTextLiveData().value?.data

    fun isLoading() = dailyTextLiveData().value?.loadStatus == AsyncLoadStatus.LOADING
    fun isError() = dailyTextLiveData().value?.loadStatus == AsyncLoadStatus.ERROR
    fun isSuccess() = dailyTextLiveData().value?.loadStatus == AsyncLoadStatus.SUCCESS

    class Factory(private val repository: DailyTextRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return DailyTextViewModel(repository) as T
        }

    }

}