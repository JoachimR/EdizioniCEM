package de.reiss.edizioni.about.list.items

import de.reiss.edizioni.util.view.StableListItem

data class SemanticsListItem(val title: String,
                             val text: String) : StableListItem() {

    override fun stableId() = hashCode().toLong()

}
