package com.example.puremusic.bridge.player.helper


import android.content.Context

import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log

/**
 * 在来电时自动协调和暂停音乐播放
 * 只为 PlayerService 服务的
 */
class PlayerCallHelper(private val mPlayerCallHelperListener: PlayerCallHelperListener?) : OnAudioFocusChangeListener {

    private var phoneStateListener: PhoneStateListener? = null  // 电话状态监听器
    private var ignoreAudioFocus = false // 忽略音频焦点
    private var mIsTempPauseByPhone = false // 是通过电话临时暂停
    private var tempPause = false // 暂停标记

    // 这里是绑定调用监听器
    fun bindCallListener(context: Context) {
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                if (state == TelephonyManager.CALL_STATE_IDLE) { // 呼叫状态空闲
                    if (mIsTempPauseByPhone) {
                        mPlayerCallHelperListener?.playAudio() // 播放音频
                        mIsTempPauseByPhone = false
                    }
                } else if (state == TelephonyManager.CALL_STATE_RINGING) { // 呼叫状态振铃
                    if (mPlayerCallHelperListener != null) {
                        if (mPlayerCallHelperListener.isPlaying &&
                            !mPlayerCallHelperListener.isPaused
                        ) {
                            mPlayerCallHelperListener.pauseAudio()
                            mIsTempPauseByPhone = true
                        }
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) { // 呼叫状态摘机
                }
                super.onCallStateChanged(state, incomingNumber)
            }
        }
        // 下面是获取电话管理器，来建立设置好监听
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        manager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE) // 对电话监听，建立绑定关系
    }



    // 这里是解除绑定调用监听器
    fun unbindCallListener(context: Context) {
        try {
            val mgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            mgr?.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        } catch (e: Exception) {
            Log.e("tmessages", e.toString())
        }
    }


    // 音频焦点变化的函数 可以修改音频焦点
    override fun onAudioFocusChange(focusChange: Int) {
        if (ignoreAudioFocus) {
            ignoreAudioFocus = false
            return
        }
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            if (mPlayerCallHelperListener != null) {
                if (mPlayerCallHelperListener.isPlaying &&
                    !mPlayerCallHelperListener.isPaused
                ) {
                    mPlayerCallHelperListener.pauseAudio()
                    tempPause = true
                }
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            if (tempPause) {
                mPlayerCallHelperListener?.playAudio()
                tempPause = false
            }
        }
    }

    // 播放监听器
    interface PlayerCallHelperListener {
        fun playAudio() // 播放音频中
        val isPlaying: Boolean // 是否播放
        val isPaused: Boolean // 是否暂停
        fun pauseAudio() // 暂停音频中
    }
}