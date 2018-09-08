package de.reiss.edizioni.downloader.list

import com.squareup.moshi.Json
import java.util.*


data class EdizioniJson(@field:Json(name = "meta") val meta: EdizioniJsonMeta = EdizioniJsonMeta(),
                        @field:Json(name = "texts") val texts: Map<String, EdizioniJsonText> = emptyMap())

data class EdizioniJsonMeta(@field:Json(name = "year") val year: Int = -1,
                            @field:Json(name = "authors") val authors: EdizioniJsonMetaAuthors = EdizioniJsonMetaAuthors(),
                            @field:Json(name = "title") val title: String = "",
                            @field:Json(name = "updated") val updated: Date = Date(0L),
                            @field:Json(name = "imageURL") val imageUrl: String = "",
                            @field:Json(name = "audioURL") val audioUrl: String = "",
                            @field:Json(name = "info") val info: List<EdizioniJsonMetaInfo> = emptyList())

data class EdizioniJsonMetaAuthors(@field:Json(name = "title") val title: String = "",
                                   @field:Json(name = "names") val names: List<String> = emptyList())


data class EdizioniJsonMetaInfo(@field:Json(name = "semantics") val semantics: String = "",
                                @field:Json(name = "title") val title: String = "",
                                @field:Json(name = "text") val text: String = "")

data class EdizioniJsonText(@field:Json(name = "verse") val verse: String = "",
                            @field:Json(name = "bibleref") val bibleRef: String = "",
                            @field:Json(name = "devotion") val devotion: List<String> = emptyList(),
                            @field:Json(name = "author") val author: String = "")