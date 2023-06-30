package com.example.puremusic.player.notification

import android.app.*
import android.content.Intent
import android.os.IBinder

import com.example.puremusic.bridge.player.PlayerManager
import com.example.puremusic.bridge.player.helper.PlayerCallHelper


/**
 * 音乐播放的服务
 * 后台音乐播放的服务
 */
class PlayerService : Service() {
    private var mPlayerCallHelper: PlayerCallHelper? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // 在播放的时候来电，怎么办， 所有需要做出处理
        if (mPlayerCallHelper == null) {
            mPlayerCallHelper = PlayerCallHelper(object :
                PlayerCallHelper.PlayerCallHelperListener {
                override fun playAudio() {
                    PlayerManager.instance.playAudio()
                }
                override val isPlaying: Boolean
                    get() = PlayerManager.instance.isPlaying
                override val isPaused: Boolean
                    get() = PlayerManager.instance.isPaused

                override fun pauseAudio() {
                    PlayerManager.instance.pauseAudio()
                }
            })
        }
        val results = PlayerManager.instance.currentPlayingMusic
        if (results == null) {
            stopSelf()
            return START_NOT_STICKY
        }
        mPlayerCallHelper!!.bindCallListener(applicationContext)

        return START_NOT_STICKY
    }

    // 解绑监听等操作
    override fun onDestroy() {
        super.onDestroy()
        mPlayerCallHelper!!.unbindCallListener(applicationContext)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}