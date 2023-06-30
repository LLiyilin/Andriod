package com.example.puremusic.bridge.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.architecture.utils.Utils
import com.example.puremusic.R
import com.example.puremusic.bridge.data.bean.TestAlbum


class HttpRequestManager private constructor() : IRemoteRequest {
    // 获取数据
    override fun getFreeMusic(liveData: MutableLiveData<TestAlbum>?) {
        val gson = Gson()
        val type = object : TypeToken<TestAlbum?>() {}.type

        val testAlbum = gson.fromJson<TestAlbum>(Utils.getApp().getString(R.string.free_music_json), type)

        liveData!!.value = testAlbum
    }

    companion object {
        val instance = HttpRequestManager()
    }
}