package de.reiss.edizioni.main.content

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.architecture.AsyncLoadStatus
import java.util.*

open class DailyTextViewModel(private val repository: DailyTextRepository) : ViewModel() {

    private val contentToDisplayLiveData: MutableLiveData<AsyncLoad<ContentToDisplay>> = MutableLiveData()

    init {
        println("DailyTextViewModel " + this.toString())
    }

    open fun contentToDisplayLiveData() = contentToDisplayLiveData

    open fun loadContentToDisplay(date: Date) {
        repository.getContentToDisplay(date, contentToDisplayLiveData())
    }

    fun contentToDisplay() = contentToDisplayLiveData().value?.data

    fun isLoading() = contentToDisplayLiveData().value?.loadStatus == AsyncLoadStatus.LOADING
    fun isError() = contentToDisplayLiveData().value?.loadStatus == AsyncLoadStatus.ERROR
    fun isSuccess() = contentToDisplayLiveData().value?.loadStatus == AsyncLoadStatus.SUCCESS

    class Factory(private val repository: DailyTextRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return DailyTextViewModel(repository) as T
        }

    }

}