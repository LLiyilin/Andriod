package com.example.puremusic.bridge.data.repository


import androidx.lifecycle.MutableLiveData

import com.example.puremusic.bridge.data.bean.TestAlbum


/**
 * 远程请求标准接口（在仓库里面）
 * 只为 HttpRequestManager 服务
 */
interface IRemoteRequest {
    fun getFreeMusic(liveData: MutableLiveData<TestAlbum>?)
}