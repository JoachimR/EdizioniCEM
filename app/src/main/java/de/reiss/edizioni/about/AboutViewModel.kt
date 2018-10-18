package de.reiss.edizioni.about

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.edizioni.about.model.About
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.architecture.AsyncLoadStatus

open class AboutViewModel(private val repository: AboutRepository) : ViewModel() {

    private val aboutLiveData: MutableLiveData<AsyncLoad<About>> = MutableLiveData()

    open fun aboutLiveData() = aboutLiveData

    open fun loadAbout() {
        repository.getAbout(aboutLiveData())
    }

    fun about() = aboutLiveData().value?.data

    fun isLoading() = aboutLiveData().value?.loadStatus == AsyncLoadStatus.LOADING
    fun isError() = aboutLiveData().value?.loadStatus == AsyncLoadStatus.ERROR
    fun isSuccess() = aboutLiveData().value?.loadStatus == AsyncLoadStatus.SUCCESS

    class Factory(private val repository: AboutRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return AboutViewModel(repository) as T
        }

    }

}