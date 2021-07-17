package com.bignerdranch.android.photogallery.api

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickrApi {

    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=74f76077448303e0a9470ff774da6f3f" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    fun fetchPhotos(): Call<PhotoResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}

class FlickrApiCreator{

    companion object {
        private var flickrApi: FlickrApi? = null
        private val gson = GsonBuilder()
            .registerTypeAdapter(PhotoResponse::class.java, PhotoDeserializer())
            .create()
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        fun create(): FlickrApi {
            if (flickrApi == null) flickrApi = retrofit.create(FlickrApi::class.java)
            return flickrApi!!
        }
    }
}