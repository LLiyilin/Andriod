package com.example.puremusic.ui

import android.os.Bundle

import androidx.databinding.DataBindingUtil
import com.example.puremusic.R

import com.example.puremusic.bridge.state.MainActivityViewModel
import com.example.puremusic.databinding.ActivityMainBinding
import com.example.puremusic.ui.base.BaseActivity


// 主页  管理者 总控中心
class MainActivity : BaseActivity() {
    var mainBinding: ActivityMainBinding? = null  // 当前MainActivity的布局
    var mainActivityViewModel: MainActivityViewModel? = null  // ViewModel

    private var isListened = false // 被倾听 为了扩展，目前还用不上

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 绑定DataBinding 与 VIewModel 结合
        mainActivityViewModel = getActivityViewModelProvider(this).get(MainActivityViewModel::class.java)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding?.lifecycleOwner = this
        mainBinding?.vm = mainActivityViewModel

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(!isListened){
            // 此字段只要发生了改变，就会 添加监听（可以弹上来的监听）
            mSharedViewModel.timeToAddSlideListener.value = true  // 触发改变
            isListened = true
        }
    }


    override fun onBackPressed() {
            // 如果把下面的代码注释掉，back键时，不会把播放详情给掉下来
            mSharedViewModel.closeSlidePanelIfExpanded.value = true // 触发改变 true 如果此时是 播放详情，会被掉下来
    }
}