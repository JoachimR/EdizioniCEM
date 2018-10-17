package de.reiss.edizioni.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.*
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.text.format.DateUtils
import android.view.MenuItem
import android.view.View
import de.reiss.edizioni.DaysPositionUtil
import de.reiss.edizioni.R
import de.reiss.edizioni.about.AboutActivity
import de.reiss.edizioni.architecture.AppActivity
import de.reiss.edizioni.audio.AudioStream
import de.reiss.edizioni.audio.AudioStreamPlayerService
import de.reiss.edizioni.events.AudioFilePlaybackChanged
import de.reiss.edizioni.events.AudioStreamProgress
import de.reiss.edizioni.main.viewpager.ViewPagerFragment
import de.reiss.edizioni.preferences.AppPreferencesActivity
import de.reiss.edizioni.util.extensions.*
import kotlinx.android.synthetic.main.audio_bottom_sheet.*
import kotlinx.android.synthetic.main.main_activity.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class MainActivity : AppActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)

    }

    private lateinit var header: Header
    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        header = Header(this)
        initNav()
        refreshFragment()
        initAudioUi()
    }

    private fun initAudioUi() {
        sheetBehavior = from(findViewById(R.id.audio_bottom_sheet))
        sheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_EXPANDED -> showOrHideSeekBar(true)
                    STATE_DRAGGING -> sheetBehavior.state = STATE_EXPANDED
                    STATE_COLLAPSED -> showOrHideSeekBar(false)
                }
            }

        })
        audio_fab.onClick {
            AudioStreamPlayerService.pauseOrUnpauseCurrentStream()
        }
        audio_close.onClick {
            AudioStreamPlayerService.stopCurrentStream()
            hidePlayer()
        }

        play_seek_bar.setPadding(0, 0, 0, 0)
        play_seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangedByUserListener() {

            override fun changedByUserTo(progress: Int) {
                AudioStreamPlayerService.seekToProgressInCurrentStream(progress.toLong())
            }


        })
    }

    override fun onStart() {
        super.onStart()
        registerToEventBus()
        updateBottomSheetState()
        updateFab()
    }

    private fun updateBottomSheetState() {
        if (AudioStreamPlayerService.isPlayingCurrentStream()) {
            sheetBehavior.state = STATE_EXPANDED
        } else {
            sheetBehavior.state = STATE_COLLAPSED
        }
    }

    override fun onStop() {
        unregisterFromEventBus()
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AudioStreamProgress) {
        audio_time_current.text = DateUtils.formatElapsedTime((event.progressInMs / 1000))
        audio_time_max.text = DateUtils.formatElapsedTime((event.durationInMs / 1000))
        play_seek_bar.post {
            play_seek_bar.progress = event.progressInMs.toInt()
            play_seek_bar.max = event.durationInMs.toInt()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AudioFilePlaybackChanged) {
        updateFab()
    }

    private fun updateFab() {
        audio_fab.setImageResource(
                when {
                    AudioStreamPlayerService.isPlayingCurrentStream() ->
                        R.drawable.ic_pause_black_24dp
                    else ->
                        R.drawable.ic_play_arrow_black_24dp
                })
    }

    fun showPlayerFor(dayPosition: Int, url: String) {
        if (sheetBehavior.state != STATE_EXPANDED) {
            sheetBehavior.state = STATE_EXPANDED
        }
        AudioStreamPlayerService.startStream(AudioStream(url, dayPosition, 0, 0))
    }

    private fun hidePlayer() {
        if (sheetBehavior.state != STATE_COLLAPSED) {
            sheetBehavior.state = STATE_COLLAPSED
        }
    }

    fun setHeader(date: Date, imageUrl: String) {
        header.setNewHeader(date, imageUrl)
    }

    private fun refreshFragment() {
        if (findFragmentIn(R.id.main_fragment_container) == null) {
            replaceFragmentIn(
                    container = R.id.main_fragment_container,
                    fragment = ViewPagerFragment.createInstance(
                            DaysPositionUtil.positionFor(Calendar.getInstance())
                    )
            )
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.main_drawer)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                goToSettings()
            }
            R.id.nav_info -> {
                startActivity(AboutActivity.createIntent(this))
            }
        }
        main_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initNav() {
        val toggle = ActionBarDrawerToggle(this,
                main_drawer,
                main_toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        main_drawer.addDrawerListener(toggle)
        toggle.syncState()

        main_nav.setNavigationItemSelectedListener(this)
    }

    private fun goToSettings() {
        startActivity(AppPreferencesActivity.createIntent(this))
    }

    private fun showOrHideSeekBar(show: Boolean) {
        play_seek_bar.animate()
                .alpha(if (show) 1.0f else 0.0f)
                .setDuration(if (show) 1 else 0)
    }

}
