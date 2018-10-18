package de.reiss.edizioni.about

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.edizioni.App
import de.reiss.edizioni.R
import de.reiss.edizioni.about.list.AboutListAdapter
import de.reiss.edizioni.about.list.AboutListBuilder
import de.reiss.edizioni.about.model.About
import de.reiss.edizioni.architecture.AppFragment
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.util.extensions.showIndefiniteSnackbar
import kotlinx.android.synthetic.main.about_fragment.*


class AboutFragment : AppFragment<AboutViewModel>(R.layout.about_fragment) {

    companion object {

        fun createInstance() = AboutFragment()

    }

    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_about, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.about_share).isEnabled = viewModel.about() != null
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.about_share -> {
                    share()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onStart() {
        super.onStart()
        tryLoad()
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, AboutViewModel.Factory(App.component.aboutRepository))

    override fun defineViewModel(): AboutViewModel =
            loadViewModelProvider().get(AboutViewModel::class.java)

    private lateinit var aboutListAdapter: AboutListAdapter

    override fun initViews() {
        aboutListAdapter = AboutListAdapter()
        with(about_recycler_view) {
            layoutManager = LinearLayoutManager(this.context!!)
            adapter = aboutListAdapter
        }
    }

    override fun initViewModelObservers() {
        viewModel.aboutLiveData().observe(this, Observer<AsyncLoad<About>> {
            updateUi()
        })
    }

    private fun updateUi() {
        val about = viewModel.about()

        when {

            viewModel.isLoading() -> {
                about_loading.loading = true
                errorSnackbar?.dismiss()
            }

            (viewModel.isError() || about == null) -> {
                about_loading.loading = false
                about_recycler_view.visibility = GONE
                errorSnackbar = this.showIndefiniteSnackbar(R.string.loading_about_not_successful)
            }

            viewModel.isSuccess() -> {
                about_loading.loading = false
                errorSnackbar?.dismiss()
                about_recycler_view.visibility = VISIBLE

                aboutListAdapter.updateContent(AboutListBuilder.buildList(about))
            }
        }

        activity?.invalidateOptionsMenu()
    }

    private fun tryLoad() {
        if (viewModel.isLoading().not()) {
            viewModel.loadAbout()
        }
    }

    private fun share() {
        startActivity(Intent.createChooser(Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.share_app, getString(R.string.play_store_url)))
                .setType("text/plain"),
                getString(R.string.share_app_title)))
    }

}