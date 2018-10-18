package de.reiss.edizioni.about.list.holders

import android.view.View
import android.widget.TextView
import de.reiss.edizioni.R
import de.reiss.edizioni.about.list.items.AppInfoListItem
import de.reiss.edizioni.util.appVersion
import de.reiss.edizioni.util.view.ListItemViewHolder
import de.reiss.edizioni.util.view.StableListItem

class AppInfoListItemViewHolder(layout: View) : ListItemViewHolder(layout) {

    private val context = layout.context
    private val version = layout.findViewById<TextView>(R.id.about_list_item_app_info_version)

    override fun bindViews(item: StableListItem) {
        if (item is AppInfoListItem) {
            this.version.text = appVersion(context)
        }
    }

}