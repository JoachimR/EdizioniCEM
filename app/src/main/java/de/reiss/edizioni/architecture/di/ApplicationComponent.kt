package de.reiss.edizioni.architecture.di

import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import dagger.Component
import de.reiss.edizioni.main.content.TheWordRepository
import de.reiss.edizioni.main.viewpager.ViewPagerRepository
import de.reiss.edizioni.note.details.NoteDetailsRepository
import de.reiss.edizioni.note.edit.EditNoteRepository
import de.reiss.edizioni.note.export.NoteExportRepository
import de.reiss.edizioni.note.list.NoteListRepository
import de.reiss.edizioni.notification.NotificationHelper
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.preferences.AppPreferencesRepository
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
            DownloaderModule::class,
            NotesExportModule::class
        ]
)
interface ApplicationComponent {

    val context: Context
    val clipboardManager: ClipboardManager

    val theWordRepository: TheWordRepository
    val viewPagerRepository: ViewPagerRepository
    val appPreferencesRepository: AppPreferencesRepository
    val editNoteRepository: EditNoteRepository
    val noteListRepository: NoteListRepository
    val noteDetailsRepository: NoteDetailsRepository
    val noteExportRepository: NoteExportRepository

    val widgetRefresher: WidgetRefresher

    val notificationHelper: NotificationHelper
    val appPreferences: AppPreferences

    val searchManager: SearchManager

}
