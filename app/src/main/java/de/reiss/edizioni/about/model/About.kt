package de.reiss.edizioni.about.model

data class About(val year: Int,
                 val title: String,
                 val semanticsItems: List<SemanticsItem>,
                 val authors: List<String>)

