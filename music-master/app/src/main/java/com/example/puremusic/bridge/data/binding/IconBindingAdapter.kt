package com.example.puremusic.bridge.data.binding

import androidx.databinding.BindingAdapter
import com.example.puremusic.ui.view.PlayPauseView

/**
 * 这里和布局建立绑定
 * 注意：这个类的使用，居然是和fragment_player.xml里面的 setIcon / isPlaying 挂钩
 */
object IconBindingAdapter {
    // 调用 播放 暂停 功能的
    @JvmStatic
    @BindingAdapter(value = ["isPlaying"], requireAll = false)
    fun isPlaying(pauseView: PlayPauseView, isPlaying: Boolean) {
        if (isPlaying) {
            pauseView.play()
        } else {
            pauseView.pause()
        }
    }

}