package de.reiss.edizioni.about.list

import de.reiss.edizioni.about.list.items.AppInfoListItem
import de.reiss.edizioni.about.list.items.AuthorListItem
import de.reiss.edizioni.about.list.items.AuthorsHeaderListItem
import de.reiss.edizioni.about.list.items.SemanticsListItem
import de.reiss.edizioni.about.model.About
import de.reiss.edizioni.util.view.StableListItem

object AboutListBuilder {

    fun buildList(about: About?) =
            mutableListOf<StableListItem>().apply {

                add(AppInfoListItem())

                if (about != null) {

                    addAll(about.semanticsItems.map { item ->
                        SemanticsListItem(item.title, item.text)
                    })

                    if (about.authors.isNotEmpty()) {
                        add(AuthorsHeaderListItem())
                        addAll(about.authors
                                .asSequence()
                                .map { authorName ->
                                    AuthorListItem(authorName)
                                }
                                .sortedBy { it.name }
                                .toList())
                    }
                }
            }
}