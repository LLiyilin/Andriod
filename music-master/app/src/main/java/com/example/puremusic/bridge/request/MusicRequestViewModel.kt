package com.example.puremusic.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.puremusic.bridge.data.bean.TestAlbum
import com.example.puremusic.bridge.data.repository.HttpRequestManager



class MusicRequestViewModel : ViewModel() {

    var freeMusicesLiveData : MutableLiveData<TestAlbum>? = null

    get() {
        if(field == null){
            field = MutableLiveData()
        }
        return field
    }
    private set


    fun requestFreeMusics() {
        HttpRequestManager.instance.getFreeMusic(freeMusicesLiveData)
    }
}