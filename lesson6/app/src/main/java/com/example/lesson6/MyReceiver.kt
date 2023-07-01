package com.example.lesson6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceiver: BroadcastReceiver() {
    val TAG = "MyReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG,"${Thread.currentThread().name} 收到了广播")
    }
}