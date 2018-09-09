package de.reiss.edizioni.architecture.di

import android.content.ClipboardManager
import android.content.Context
import dagger.Component
import de.reiss.edizioni.download.DownloadAndStore
import de.reiss.edizioni.main.content.DailyTextRepository
import de.reiss.edizioni.main.viewpager.ViewPagerRepository
import de.reiss.edizioni.notification.NotificationHelper
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.widget.WidgetRefresher

@ApplicationScope
@Component(
        modules = [
            ContextModule::class,
            AndroidModule::class,
            DatabaseModule::class,
            PreferenceModule::class,
            ExecutorModule::class,
            OkHttpModule::class,
            RetrofitModule::class,
            DownloaderModule::class
        ]
)
interface ApplicationComponent {

    val context: Context
    val clipboardManager: ClipboardManager

    val dailyTextRepository: DailyTextRepository
    val viewPagerRepository: ViewPagerRepository

    val downloadAndStore: DownloadAndStore

    val widgetRefresher: WidgetRefresher

    val notificationHelper: NotificationHelper
    val appPreferences: AppPreferences
}
