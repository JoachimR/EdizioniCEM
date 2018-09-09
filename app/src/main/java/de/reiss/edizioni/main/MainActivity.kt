package de.reiss.edizioni.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.graphics.Palette
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import de.reiss.edizioni.DaysPositionUtil
import de.reiss.edizioni.R
import de.reiss.edizioni.about.AboutActivity
import de.reiss.edizioni.architecture.AppActivity
import de.reiss.edizioni.architecture.GlideApp
import de.reiss.edizioni.formattedDate
import de.reiss.edizioni.main.viewpager.ViewPagerFragment
import de.reiss.edizioni.preferences.AppPreferencesActivity
import de.reiss.edizioni.util.extensions.findFragmentIn
import de.reiss.edizioni.util.extensions.replaceFragmentIn
import de.reiss.edizioni.util.extensions.visibleElseGone
import kotlinx.android.synthetic.main.main_activity.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)

    }

    private var isToolbarCollapsed = false

    private object dateInfo {
        var formattedDate: String = ""
        var weekDay: String = ""
        var dayOfMonth: String = ""
        var month: String = ""
        var year: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setupToolbar()

        initNav()
        refreshFragment()
    }

    private fun setupToolbar() {
        setSupportActionBar(collapsing_toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        collapsing_toolbar_layout.title = ""

        class ToolbarCollapsedListener : AppBarLayout.OnOffsetChangedListener {

            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    isToolbarCollapsed = true
                } else if (isToolbarCollapsed) {
                    isToolbarCollapsed = false
                }

                refreshToolbarTitle()
            }
        }

        main_app_bar.addOnOffsetChangedListener(ToolbarCollapsedListener())
    }

    private fun refreshToolbarTitle() {
        if (isToolbarCollapsed) {
            collapsing_toolbar_layout.title = dateInfo.formattedDate
            collapsing_toolbar_date_view_root.visibleElseGone(false)
        } else {
            collapsing_toolbar_layout.title = ""
            collapsing_toolbar_date_view_day_of_month.text = dateInfo.dayOfMonth
            collapsing_toolbar_date_view_weekday.text = dateInfo.weekDay
            collapsing_toolbar_date_view_month.text = dateInfo.month
            collapsing_toolbar_date_view_year.text = dateInfo.year
            collapsing_toolbar_date_view_root.visibleElseGone(true)
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

    fun setHeader(date: Date, imageUrl: String) {
        if (URLUtil.isValidUrl(imageUrl)) {
            GlideApp.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .into(object : BitmapImageViewTarget(collapsing_toolbar_image) {

                        override fun onResourceReady(resource: Bitmap,
                                                     transition: Transition<in Bitmap>?) {
                            super.onResourceReady(resource, transition)

                            @Suppress("DEPRECATION")
                            val defaultColor = resources.getColor(R.color.colorPrimary);
                            Palette.from(resource).generate { palette ->
                                fillCustomGradient(collapsing_toolbar_date_view_root,
                                        palette.getDarkVibrantColor(defaultColor),
                                        palette.getLightVibrantColor(defaultColor))

                                val locale = Locale.getDefault()

                                dateInfo.formattedDate = formattedDate(this@MainActivity, date.time)
                                dateInfo.weekDay = SimpleDateFormat("EEEE", locale).format(date)
                                dateInfo.dayOfMonth = SimpleDateFormat("dd", locale).format(date)
                                dateInfo.month = SimpleDateFormat("MMM", locale).format(date)
                                dateInfo.year = SimpleDateFormat("yyyy", locale).format(date)
                                refreshToolbarTitle()
                            }
                        }
                    })
        }
    }

    private fun fillCustomGradient(view: View,
                                   @ColorInt color0: Int,
                                   @ColorInt color1: Int) {
        val layers = arrayOfNulls<Drawable>(1)

        val shaderFactory = object : ShapeDrawable.ShaderFactory() {

            override fun resize(width: Int, height: Int): Shader {
                return LinearGradient(0f, 0f, view.width.toFloat(), 0f,
                        color0, color1,
                        Shader.TileMode.CLAMP)
            }

        }

        val paintDrawable = PaintDrawable()
        paintDrawable.shape = RectShape()
        paintDrawable.shaderFactory = shaderFactory
        paintDrawable.setCornerRadii(floatArrayOf(5f, 5f, 5f, 5f, 0f, 0f, 0f, 0f))
        layers[0] = paintDrawable as Drawable

        view.background = LayerDrawable(layers)
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

    private fun initNav() {
        val toggle = ActionBarDrawerToggle(this,
                main_drawer,
                collapsing_toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        main_drawer.addDrawerListener(toggle)
        toggle.syncState()

        main_nav.setNavigationItemSelectedListener(this)
    }

    private fun goToSettings() {
        startActivity(AppPreferencesActivity.createIntent(this))
    }

}
