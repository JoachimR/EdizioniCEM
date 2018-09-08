package de.reiss.edizioni.preferences

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class AppPreferencesViewModel() : ViewModel() {

    class Factory() : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return AppPreferencesViewModel() as T
        }

    }

}