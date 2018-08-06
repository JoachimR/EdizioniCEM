package de.reiss.edizioni.downloader.list

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface EdizioniJsonService {

    @GET("calendar")
    fun calendar(): Call<EdizioniJson>
//    fun calendar(@Query("year") year: Int): Call<EdizioniJson>

}