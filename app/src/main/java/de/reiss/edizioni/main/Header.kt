package de.reiss.edizioni.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.support.annotation.ColorInt
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import de.reiss.edizioni.R
import de.reiss.edizioni.architecture.GlideApp
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.util.extensions.visibleElseGone
import java.util.*

class Header(private val mainActivity: MainActivity,
             private val appPreferences: AppPreferences) {

    init {
        Locale.setDefault(Locale.ITALIAN)
    }

    private val viewAppBar: AppBarLayout = mainActivity.findViewById(R.id.main_app_bar)
    private val viewCollapsingToolbarLayout: CollapsingToolbarLayout = mainActivity.findViewById(R.id.collapsing_toolbar_layout)
    private val viewToolbar: Toolbar = mainActivity.findViewById(R.id.main_toolbar)
    private val viewDateRoot: View = mainActivity.findViewById(R.id.collapsing_toolbar_date_view_root)
    private val viewDayOfMonth: TextView = mainActivity.findViewById(R.id.collapsing_toolbar_date_view_day_of_month)
    private val viewWeekday: TextView = mainActivity.findViewById(R.id.collapsing_toolbar_date_view_weekday)
    private val viewMonth: TextView = mainActivity.findViewById(R.id.collapsing_toolbar_date_view_month)
    private val viewYear: TextView = mainActivity.findViewById(R.id.collapsing_toolbar_date_view_year)
    private val viewImage: ImageView = mainActivity.findViewById(R.id.collapsing_toolbar_image)

    private val headerCalendar = HeaderCalendar(Date())
    private var currentImageUrl: String? = null

    private var isToolbarCollapsed = false

    init {
        mainActivity.setSupportActionBar(viewToolbar)
        val actionBar = mainActivity.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        viewCollapsingToolbarLayout.title = ""

        viewAppBar.addOnOffsetChangedListener(ToolbarCollapsedListener())
    }

    fun setNewHeader(date: Date, imageUrl: String?) {
        headerCalendar.date = date
        refreshToolbarText()
        refreshCurrentImageAsync(imageUrl)
    }

    private fun refreshToolbarText() {
        if (isToolbarCollapsed) {
            viewCollapsingToolbarLayout.title = headerCalendar.formattedDate(mainActivity)
            viewDateRoot.visibleElseGone(false)
        } else {
            viewCollapsingToolbarLayout.title = ""
            viewDayOfMonth.text = headerCalendar.dayOfMonth()
            viewWeekday.text = headerCalendar.weekDay()
            viewMonth.text = headerCalendar.month()
            viewYear.text = headerCalendar.year()
            viewDateRoot.visibleElseGone(true)
        }
    }

    private fun refreshCurrentImageAsync(imageUrl: String?) {
        val urlChanged = currentImageUrl != imageUrl
        if (urlChanged) {
            currentImageUrl = imageUrl
        }
        if (urlChanged) {
            refreshImageAsync(currentImageUrl) { success ->
                refreshToolbarText()
                if (success) {
                    appPreferences.setLastUsedImageUrl(currentImageUrl)
                }
            }
        }
    }

    private fun refreshImageAsync(imageUrl: String?, onFinish: (Boolean) -> Unit) {
        val url: String? = imageUrl ?: appPreferences.getLastUsedImageUrl()
        GlideApp.with(mainActivity)
                .asBitmap()
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?,
                                              model: Any?,
                                              target: Target<Bitmap>?,
                                              isFirstResource: Boolean): Boolean {
                        drawCalendarBackgroundColorAsync(null, onFinish)
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap?,
                                                 model: Any?,
                                                 target: Target<Bitmap>?,
                                                 dataSource: DataSource?,
                                                 isFirstResource: Boolean): Boolean {
                        val bitmap = resource ?: loadDefaultBanner()
                        drawCalendarBackgroundColorAsync(bitmap, onFinish)
                        return false
                    }
                })
                .load(url)
                .placeholder(R.drawable.default_banner)
                .error(R.drawable.default_banner)
                .into(viewImage)
    }

    fun loadDefaultBanner(): Bitmap =
            BitmapFactory.decodeResource(mainActivity.resources, R.drawable.default_banner)

    fun drawCalendarBackgroundColorAsync(bitmap: Bitmap?, onFinish: (Boolean) -> Unit) {
        if (bitmap == null) {
            @Suppress("DEPRECATION")
            viewDateRoot.setBackgroundColor(
                    mainActivity.resources.getColor(android.R.color.transparent))
            onFinish(false)
        } else {
            @Suppress("DEPRECATION")
            val defaultColor = mainActivity.resources.getColor(R.color.colorPrimary)
            Palette.from(bitmap).generate { palette ->
                palette?.let {
                    fillCustomGradient(viewDateRoot,
                            it.getDarkVibrantColor(defaultColor),
                            it.getLightVibrantColor(defaultColor))
                }
                onFinish(true)
            }
        }
    }

    private fun fillCustomGradient(view: View,
                                   @ColorInt color0: Int,
                                   @ColorInt color1: Int) {
        val linearShaderFactory = object : ShapeDrawable.ShaderFactory() {

            override fun resize(width: Int, height: Int): Shader {
                return LinearGradient(0f, 0f, view.width.toFloat(), 0f,
                        color0, color1,
                        Shader.TileMode.CLAMP)
            }

        }

        val paintDrawable = PaintDrawable().apply {
            shape = RectShape()
            shaderFactory = linearShaderFactory
            setCornerRadii(floatArrayOf(5f, 5f, 5f, 5f, 0f, 0f, 0f, 0f))
        }

        view.background = LayerDrawable(arrayOf(paintDrawable))
    }

    inner class ToolbarCollapsedListener : AppBarLayout.OnOffsetChangedListener {

        private var scrollRange = -1

        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }

            if (scrollRange + verticalOffset == 0) {
                isToolbarCollapsed = true
            } else if (isToolbarCollapsed) {
                isToolbarCollapsed = false
            }

            refreshToolbarText()
        }
    }

}