package com.example.lesson6

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn).setOnClickListener{

            Thread{
                val castIntent = Intent("cn.edu.swu.cs.android.lesson6.MY_CAST")
                castIntent.setPackage(packageName)
                castIntent.putExtra("name","swu")
                sendBroadcast(castIntent)

                Log.d(TAG,"${Thread.currentThread().name} send cast.")
            }.start()

        }

    //动态注册广播接收器
        registerReceiver(MyReceiver(), IntentFilter("cn.edu.swu.cs.android.lesson6.MY_CAST"))
    }

}