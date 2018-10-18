package de.reiss.edizioni.about.list.holders

import android.view.View
import android.widget.TextView
import de.reiss.edizioni.R
import de.reiss.edizioni.about.list.items.AuthorListItem
import de.reiss.edizioni.util.view.ListItemViewHolder
import de.reiss.edizioni.util.view.StableListItem

class AuthorListItemViewHolder(layout: View) : ListItemViewHolder(layout) {

    private val name = layout.findViewById<TextView>(R.id.about_list_item_author_name)

    private var item: AuthorListItem? = null

    override fun bindViews(item: StableListItem) {
        if (item is AuthorListItem) {
            this.item = item
            this.name.text = item.name
        }
    }

}