package de.reiss.edizioni.architecture.di

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import de.reiss.edizioni.downloader.list.CalendarDownloader
import de.reiss.edizioni.downloader.list.CalendarFromRawDownloader
import de.reiss.edizioni.downloader.list.EdizioniJsonService

@Module(
        includes = [
            OkHttpModule::class,
            RetrofitModule::class
        ]
)
open class DownloaderModule {

    @Provides
    @ApplicationScope
    open fun calendarDownloader(edizioniJsonService: EdizioniJsonService): CalendarDownloader =
            CalendarDownloader(edizioniJsonService)

    @Provides
    @ApplicationScope
    open fun calendarFromRawLoader(context: Context, moshi: Moshi): CalendarFromRawDownloader =
            CalendarFromRawDownloader(context, moshi)

}