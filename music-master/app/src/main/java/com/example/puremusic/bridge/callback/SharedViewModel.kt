package com.example.puremusic.bridge.callback


import androidx.lifecycle.ViewModel
import com.example.architecture.bridge.callback.UnPeekLiveData


/**
 * SharedViewModel的职责是用于 页面通信的
 */
class SharedViewModel : ViewModel() {
    // 添加监听（可以弹上来的监听）
    // 是为了 sliding.addPanelSlideListener(new PlayerSlideListener(mBinding, sliding));
    val timeToAddSlideListener = UnPeekLiveData<Boolean>()

    // 可以控制 播放详情 点击/back 掉下来
    // 播放详情中 左手边滑动图标(点击的时候)，与 MainActivity back 是 会set(true)
    //  ----> 如果是扩大，也就是 详情页面展示了出来，就会被掉下来
    val closeSlidePanelIfExpanded = UnPeekLiveData<Boolean>()

    // 活动关闭的一些记录（播放条 缩小一条 与 扩大展开） 通知给 控制者的（扩展的）
    val activityCanBeClosedDirectly = UnPeekLiveData<Boolean>()


}