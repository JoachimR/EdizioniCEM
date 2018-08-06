package de.reiss.edizioni.preferences

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.architecture.AsyncLoadStatus
import de.reiss.edizioni.model.Bible

class AppPreferencesViewModel(private val repository: AppPreferencesRepository) : ViewModel() {

    var biblesLiveData: MutableLiveData<AsyncLoad<List<Bible>>> = MutableLiveData()

    fun bibles() = biblesLiveData.value?.data ?: emptyList()

    fun loadBibles() {
        repository.loadBibleItems(biblesLiveData)
    }

    fun isLoadingBibles() = biblesLiveData.value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val repository: AppPreferencesRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return AppPreferencesViewModel(repository) as T
        }

    }

}