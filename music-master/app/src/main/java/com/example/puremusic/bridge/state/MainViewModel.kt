package com.example.puremusic.bridge.state

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * 首页Fragment 的 MainViewModel
 */
class MainViewModel: ViewModel() {
// MainFragment初始化页面的标记 初始化选项卡和页面
    @JvmField
    val initTabAndPage = ObservableBoolean()

    @JvmField // 剔除set
    val pageAssetPath = ObservableField<String>()
}