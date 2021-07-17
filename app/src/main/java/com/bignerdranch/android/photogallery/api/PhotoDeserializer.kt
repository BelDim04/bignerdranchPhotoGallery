package com.bignerdranch.android.photogallery.api

import android.util.Log
import com.bignerdranch.android.photogallery.GalleryItem
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class PhotoDeserializer : JsonDeserializer<PhotoResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PhotoResponse {
        val photoResponse = PhotoResponse()
        val jsonObject = json?.asJsonObject
        val node = jsonObject?.get("photos")?.asJsonObject
        val photos = node?.get("photo")?.asJsonArray
        if (photos != null) {
            photoResponse.galleryItems =  List(photos.size()) { GalleryItem(photos[it].asJsonObject.get("title").asString,
                photos[it].asJsonObject.get("id").asString,
                photos[it].asJsonObject.get("url_s").asString) }
        }
        return photoResponse
    }
}