package com.bignerdranch.android.photogallery.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.photogallery.GalleryItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetchr"

class FlickrFetchr(private val flickrApi: FlickrApi) {

    private var flickrRequest: Call<PhotoResponse>? = null

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        flickrRequest= flickrApi.fetchPhotos()

        flickrRequest!!.enqueue(object : Callback<PhotoResponse> {
            override fun onResponse(call: Call<PhotoResponse>, response: Response<PhotoResponse>) {
                Log.d(TAG, "Response received")
                val photoResponse: PhotoResponse? = response.body()
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }

            override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

        })

        return responseLiveData
    }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap?{
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }

    fun cancelRequestInFlight(){
        flickrRequest?.cancel()
    }
}