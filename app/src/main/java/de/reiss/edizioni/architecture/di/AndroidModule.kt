package de.reiss.edizioni.architecture.di

import android.content.ClipboardManager
import android.content.Context
import dagger.Module
import dagger.Provides


@Module(
        includes = [
            ContextModule::class
        ]
)
class AndroidModule {

    @Provides
    @ApplicationScope
    fun clipboardManager(context: Context) =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

}