package com.example.architecture.data.manager

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.*

/**
 * 观察者 眼睛  观察所有Activity的状态
 * 就是不可见不提示 的 控制 DefaultLifecycleObserver 管理的
 * 此观察者 去 观察 BaseActivity 的 生命周期方法
 */
class NetworkStateManager private constructor() : DefaultLifecycleObserver {

    private var mNetworkStateReceive: NetworkStateReceive? = null

    /**
     * 那么观察到 观察 BaseActivity 的 生命周期方法 后 做什么事情呢？
     * 答；就是注册一个 广播，此广播可以接收到信息（然后 输出 “网络不给力”）
     * @param owner
     */
    override fun onResume(owner: LifecycleOwner) {
        mNetworkStateReceive = NetworkStateReceive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        if (owner is AppCompatActivity) {
            owner.registerReceiver(mNetworkStateReceive, filter)
        } else if (owner is Fragment) {
            Objects.requireNonNull(owner.activity) ?.registerReceiver(mNetworkStateReceive, filter)
        }
    }

    /**
     * 那么观察到 观察 BaseActivity 的 生命周期方法 后 做什么事情呢？
     * 答；就是移除一个 广播
     * @param owner
     */
    override fun onPause(owner: LifecycleOwner) {
        if (owner is AppCompatActivity) {
            owner.unregisterReceiver(mNetworkStateReceive)
        } else if (owner is Fragment) {
            Objects.requireNonNull(owner.activity) ?.unregisterReceiver(mNetworkStateReceive)
        }
    }

    companion object {
        val instance = NetworkStateManager()
    }
}