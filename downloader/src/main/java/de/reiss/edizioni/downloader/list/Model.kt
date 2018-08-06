package de.reiss.edizioni.downloader.list

import com.squareup.moshi.Json
import java.util.*


data class EdizioniJson(@field:Json(name = "info") val info: EdizioniJsonInfo = EdizioniJsonInfo(),
                        @field:Json(name = "texts") val texts: List<EdizioniJsonText> = emptyList())

data class EdizioniJsonInfo(@field:Json(name = "year") val year: Int = -1,
                            @field:Json(name = "authors") val authors: EdizioniJsonInfoAuthors = EdizioniJsonInfoAuthors(),
                            @field:Json(name = "title") val title: String = "",
                            @field:Json(name = "imageUrl") val imageUrl: String = "",
                            @field:Json(name = "contact") val contact: EdizioniJsonInfoContact = EdizioniJsonInfoContact(),
                            @field:Json(name = "about") val about: EdizioniJsonInfoAbout = EdizioniJsonInfoAbout(),
                            @field:Json(name = "objective") val objective: EdizioniJsonInfoObjective = EdizioniJsonInfoObjective(),
                            @field:Json(name = "team") val team: EdizioniJsonInfoTeam = EdizioniJsonInfoTeam())

data class EdizioniJsonInfoAuthors(@field:Json(name = "title") val title: String = "",
                                   @field:Json(name = "names") val names: List<String> = emptyList())


data class EdizioniJsonInfoContact(@field:Json(name = "title") val title: String = "",
                                   @field:Json(name = "address") val address: String = "",
                                   @field:Json(name = "phone") val phone: String = "",
                                   @field:Json(name = "email") val email: String = "",
                                   @field:Json(name = "web") val web: String = "")

data class EdizioniJsonInfoAbout(@field:Json(name = "title") val title: String = "",
                                 @field:Json(name = "text") val text: String = "")

data class EdizioniJsonInfoObjective(@field:Json(name = "title") val title: String = "",
                                     @field:Json(name = "text") val text: String = "")

data class EdizioniJsonInfoTeam(@field:Json(name = "title") val title: String = "",
                                @field:Json(name = "text") val text: String = "")

data class EdizioniJsonText(@field:Json(name = "date") val date: Date,
                            @field:Json(name = "title") val title: String,
                            @field:Json(name = "verse") val verse: String,
                            @field:Json(name = "bibleref") val bibleRef: String,
                            @field:Json(name = "devotion") val devotion: List<String>,
                            @field:Json(name = "author") val author: String)