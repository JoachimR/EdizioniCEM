package de.reiss.edizioni.about.list.holders

import android.view.View
import android.widget.TextView
import de.reiss.edizioni.R
import de.reiss.edizioni.about.list.items.SemanticsListItem
import de.reiss.edizioni.util.view.ListItemViewHolder
import de.reiss.edizioni.util.view.StableListItem

class SemanticsListItemViewHolder(layout: View) : ListItemViewHolder(layout) {

    private val title = layout.findViewById<TextView>(R.id.about_list_item_title)
    private val text = layout.findViewById<TextView>(R.id.about_list_item_text)

    private var item: SemanticsListItem? = null

    override fun bindViews(item: StableListItem) {
        if (item is SemanticsListItem) {
            this.item = item
            this.title.text = item.title
            this.text.text = item.text
        }
    }

}