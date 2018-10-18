package de.reiss.edizioni.about.list.items

import de.reiss.edizioni.util.view.StableListItem

data class AuthorListItem(val name: String) : StableListItem() {

    override fun stableId() = hashCode().toLong()

}
