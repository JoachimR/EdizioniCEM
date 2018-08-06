package de.reiss.edizioni.main.viewpager


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.edizioni.App
import de.reiss.edizioni.DaysPositionUtil
import de.reiss.edizioni.R
import de.reiss.edizioni.architecture.AppFragment
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.events.DatabaseRefreshed
import de.reiss.edizioni.events.JsonDownloadRequested
import de.reiss.edizioni.events.ViewPagerMoveRequest
import de.reiss.edizioni.events.postMessageEvent
import de.reiss.edizioni.util.extensions.registerToEventBus
import de.reiss.edizioni.util.extensions.unregisterFromEventBus
import kotlinx.android.synthetic.main.view_pager_fragment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class ViewPagerFragment : AppFragment<ViewPagerViewModel>(R.layout.view_pager_fragment) {

    companion object {

        private val KEY_INITIAL_POS = "KEY_INITIAL_POS"
        private val KEY_CURRENT_POSITION = "KEY_CURRENT_POSITION"

        private val INVALID_POSITION = -1

        fun createInstance(position: Int? = null) = ViewPagerFragment().apply {
            arguments = Bundle().apply {
                if (position != null) {
                    putInt(KEY_INITIAL_POS, position)
                }
            }
        }
    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private var savedPosition = INVALID_POSITION

    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPosition(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        registerToEventBus()
    }

    override fun onStop() {
        unregisterFromEventBus()
        super.onStop()
    }

    private fun loadPosition(savedInstanceState: Bundle?) {
        val initialPos = arguments?.getInt(KEY_INITIAL_POS, INVALID_POSITION) ?: -1
        arguments?.remove(KEY_INITIAL_POS)
        savedPosition = when {
            initialPos != INVALID_POSITION -> initialPos
            else -> savedInstanceState?.getInt(KEY_CURRENT_POSITION)
                    ?: DaysPositionUtil.positionFor(Calendar.getInstance())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_CURRENT_POSITION, currentPosition())
        super.onSaveInstanceState(outState)
    }

    override fun initViews() {
        adapter = ViewPagerAdapter(childFragmentManager)
        view_pager.adapter = adapter
    }

    override fun defineViewModelProvider() = ViewModelProviders.of(this,
            ViewPagerViewModel.Factory(App.component.viewPagerRepository))

    override fun defineViewModel(): ViewPagerViewModel =
            loadViewModelProvider().get(ViewPagerViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.loadYearLiveData().observe(this, Observer<AsyncLoad<Void>> {
            updateUi()
        })
    }

    override fun onAppFragmentReady() {
        goToPosition(savedPosition)
        updateUi()
        tryRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: JsonDownloadRequested) {
        if (viewModel.isLoadingContent().not()) {
            viewModel.prepareContentFor(date = event.year.time)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ViewPagerMoveRequest) {
        goToPosition(event.position)
    }

    private fun updateUi() {
        if (viewModel.isLoadingContent()) {
            view_pager_loading_text.text = getString(R.string.view_pager_loading_content)
            view_pager_loading.visibility = VISIBLE
            view_pager.visibility = GONE
        } else {
            view_pager_loading.visibility = GONE
            view_pager.visibility = VISIBLE
            postMessageEvent(DatabaseRefreshed())
        }
    }

    private fun tryRefresh() {
        if (viewModel.isLoadingContent().not()) {
            viewModel.prepareContentFor(DaysPositionUtil.dayFor(savedPosition).time)
        }
    }

    private fun goToPosition(positionInFocus: Int) {
        view_pager.currentItem = positionInFocus
    }

    private fun currentPosition() = view_pager.currentItem

}
