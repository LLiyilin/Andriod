package com.example.puremusic.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.puremusic.App
import com.example.puremusic.bridge.callback.SharedViewModel

open class BaseFragment : Fragment() {
    protected var mActivity: AppCompatActivity? = null // 为了 让所有的子类 持有 Activity

    // private var _sharedViewModel: SharedViewModel
    // 贯穿整个项目的（只会让App(Application)初始化一次）
    protected lateinit var sharedViewModel: SharedViewModel // 共享区域的ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = getAppViewModelProvider().get(SharedViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    // 给当前BaseFragment用的【共享区域的ViewModel】
    protected fun getAppViewModelProvider(): ViewModelProvider {
        return (mActivity!!.applicationContext as App).getAppViewModelProvider(mActivity!!)
    }

    // 给所有的 子fragment提供的函数，可以顺利的拿到 ViewModel 【非共享区域的ViewModel】
    protected fun getFragmentViewModelProvider(fragment: Fragment): ViewModelProvider {
        return ViewModelProvider(fragment, fragment.defaultViewModelProviderFactory)
    }

}