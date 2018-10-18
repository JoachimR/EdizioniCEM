package de.reiss.edizioni.about.list

import android.view.ViewGroup
import de.reiss.edizioni.R
import de.reiss.edizioni.about.list.holders.AppInfoListItemViewHolder
import de.reiss.edizioni.about.list.holders.AuthorListItemViewHolder
import de.reiss.edizioni.about.list.holders.AuthorsHeaderListItemViewHolder
import de.reiss.edizioni.about.list.holders.SemanticsListItemViewHolder
import de.reiss.edizioni.about.list.items.AppInfoListItem
import de.reiss.edizioni.about.list.items.AuthorListItem
import de.reiss.edizioni.about.list.items.AuthorsHeaderListItem
import de.reiss.edizioni.about.list.items.SemanticsListItem
import de.reiss.edizioni.util.view.StableListItemAdapter

class AboutListAdapter() : StableListItemAdapter() {

    override fun getItemViewType(position: Int) =
            when (getItem(position)) {

                is AppInfoListItem -> R.layout.about_list_item_app_info
                is SemanticsListItem -> R.layout.about_list_item_semantics
                is AuthorsHeaderListItem -> R.layout.about_list_item_authors_header
                is AuthorListItem -> R.layout.about_list_item_author

                else -> -1
            }


    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
            when (viewType) {

                R.layout.about_list_item_app_info -> {
                    AppInfoListItemViewHolder(inflate(group, viewType))
                }
                R.layout.about_list_item_semantics -> {
                    SemanticsListItemViewHolder(inflate(group, viewType))
                }
                R.layout.about_list_item_authors_header -> {
                    AuthorsHeaderListItemViewHolder(inflate(group, viewType))
                }
                R.layout.about_list_item_author -> {
                    AuthorListItemViewHolder(inflate(group, viewType))
                }

                else -> throw IllegalStateException("Invalid viewType $viewType")
            }

}
