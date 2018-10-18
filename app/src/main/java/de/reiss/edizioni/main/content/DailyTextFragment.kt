package de.reiss.edizioni.main.content

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.edizioni.App
import de.reiss.edizioni.DaysPositionUtil
import de.reiss.edizioni.R
import de.reiss.edizioni.architecture.AppFragment
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.events.DatabaseRefreshed
import de.reiss.edizioni.events.FontSizeChanged
import de.reiss.edizioni.events.JsonDownloadRequested
import de.reiss.edizioni.events.postMessageEvent
import de.reiss.edizioni.main.MainActivity
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.preferences.AppPreferencesActivity
import de.reiss.edizioni.util.copyToClipboard
import de.reiss.edizioni.util.extensions.*
import de.reiss.edizioni.util.htmlize
import kotlinx.android.synthetic.main.daily_text.*
import kotlinx.android.synthetic.main.daily_text_content.*
import kotlinx.android.synthetic.main.daily_text_empty.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class DailyTextFragment : AppFragment<DailyTextViewModel>(R.layout.daily_text) {

    companion object {

        private const val KEY_POSITION = "KEY_POSITION"

        fun createInstance(position: Int) = DailyTextFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_POSITION, position)
            }
        }

    }

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    private var position = -1

    private var isInFocus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        position = arguments?.getInt(KEY_POSITION, -1) ?: -1
        if (position < 0) {
            throw IllegalStateException("date position unknown")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_share).isEnabled = viewModel.contentToDisplay() != null
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_share -> {
                    share()
                    true
                }
                R.id.menu_font_size -> {
                    displayDialog(FontSizePreferenceDialog.createInstance())
                    true
                }
                R.id.menu_date_pick -> {
                    displayDialog(ChooseDayDialog.createInstance(position))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isInFocus = isVisibleToUser
    }

    override fun onStart() {
        super.onStart()
        registerToEventBus()
        refreshAudioVisibility()
        tryLoad()
    }

    override fun onStop() {
        unregisterFromEventBus()
        super.onStop()
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, DailyTextViewModel.Factory(
                    App.component.dailyTextRepository))

    override fun defineViewModel(): DailyTextViewModel =
            loadViewModelProvider().get(DailyTextViewModel::class.java)

    override fun initViews() {
        daily_text_try_download.onClick {
            postMessageEvent(JsonDownloadRequested(year = DaysPositionUtil.dayFor(position)
            ))
        }

        daily_text_change_translation.onClick {
            activity?.let {
                it.startActivity(AppPreferencesActivity.createIntent(it))
            }
        }

        daily_text_ref1.onClick {
            context?.let { context ->
                copyToClipboard(context, daily_text_ref1.text.toString())
                showShortSnackbar(message = R.string.copied_to_clipboard)
            }
        }

        daily_text_audio.onClick {
            viewModel.contentToDisplay()?.yearInfo?.audioUrl?.let { audioUrl ->
                (activity as MainActivity).showPlayerFor(position, audioUrl)
            }
        }
    }

    fun setMainHeader() {
        (activity as? MainActivity)?.setHeader(
                date = date(),
                imageUrl = viewModel.contentToDisplay()?.yearInfo?.imageUrl)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: DatabaseRefreshed) {
        tryLoad()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FontSizeChanged) {
        updateStyle()
    }

    override fun initViewModelObservers() {
        viewModel.contentToDisplayLiveData().observe(this, Observer<AsyncLoad<ContentToDisplay>> {
            updateUi()
        })
    }

    private fun updateUi() {
        val contentToDisplay = viewModel.contentToDisplay()

        when {

            viewModel.isLoading() -> {
                daily_text_loading.loading = true
            }

            (viewModel.isError() || contentToDisplay == null) -> {
                daily_text_loading.loading = false
                daily_text_empty_root.visibility = VISIBLE
                daily_text_content_root.visibility = GONE
            }

            viewModel.isSuccess() -> {
                daily_text_loading.loading = false
                daily_text_empty_root.visibility = GONE
                daily_text_content_root.visibility = VISIBLE

                daily_text_text1.text = htmlize(contentToDisplay.dailyText.verse)
                daily_text_ref1.text = contentToDisplay.dailyText.bibleRef
                daily_text_text2.text = htmlize(contentToDisplay.dailyText.devotions.joinToString("<br><br>"))
                daily_text_ref2.text = contentToDisplay.dailyText.author
            }
        }

        activity?.invalidateOptionsMenu()

        updateStyle()
    }

    private fun updateStyle() {
        val size = appPreferences.fontSize()
        val contentSize = (size * 1.1).toFloat()
        val refSize = (size * 0.7).toFloat()

        daily_text_text1.textSize = contentSize
        daily_text_ref1.textSize = refSize
        daily_text_text2.textSize = contentSize
        daily_text_ref2.textSize = refSize
    }

    private fun tryLoad() {
        val date = date()

        if (viewModel.isLoading().not()) {
            viewModel.loadContentToDisplay(date)
        }
    }

    private fun date() = DaysPositionUtil.dayFor(position).time

    private fun share() {
        context?.let { context ->
            viewModel.contentToDisplay()?.let { contentToDisplay ->
                displayDialog(ShareDialog.createInstance(
                        context = context,
                        dailyText = contentToDisplay.dailyText
                ))
            }
        }
    }

    private fun refreshAudioVisibility() = daily_text_audio.visibleElseGone(shouldShowAudio())

    private fun shouldShowAudio() = DaysPositionUtil.positionFor(Calendar.getInstance()) == position

}