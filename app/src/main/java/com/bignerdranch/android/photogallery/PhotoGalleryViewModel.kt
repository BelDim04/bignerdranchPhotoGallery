package com.bignerdranch.android.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.api.FlickrApi
import com.bignerdranch.android.photogallery.api.FlickrApiCreator
import com.bignerdranch.android.photogallery.api.FlickrFetchr

class PhotoGalleryViewModel: ViewModel() {

    val flickrFetchr: FlickrFetchr
    val galleryItemLiveData: LiveData<List<GalleryItem>>

    init {
        flickrFetchr = FlickrFetchr(FlickrApiCreator.create())
        galleryItemLiveData = flickrFetchr.fetchPhotos()
    }

    override fun onCleared() {
        super.onCleared()
        flickrFetchr.cancelRequestInFlight()
    }
}