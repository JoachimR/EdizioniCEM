package de.reiss.edizioni.main.viewpager

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.architecture.AsyncLoadStatus
import de.reiss.edizioni.util.extensions.extractYear
import java.util.*

open class ViewPagerViewModel(private val repository: ViewPagerRepository) : ViewModel() {

    private val loadYearLiveData: MutableLiveData<AsyncLoad<Void>> = MutableLiveData()

    init {
        loadYearLiveData.postValue(AsyncLoad.loading())
    }

    open fun loadYearLiveData() = loadYearLiveData

    open fun prepareContentFor(date: Date) {
        repository.getItemsFor(date.extractYear(), loadYearLiveData())
    }

    fun isLoadingContent() = loadYearLiveData().value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val repository: ViewPagerRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return ViewPagerViewModel(repository) as T
        }

    }

}