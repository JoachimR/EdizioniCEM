package de.reiss.edizioni.about.list.items

import de.reiss.edizioni.util.view.StableListItem

class AppInfoListItem : StableListItem() {

    override fun stableId() = javaClass.name.hashCode().toLong()

}
