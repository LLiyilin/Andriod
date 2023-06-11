package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreated..")
        setContentView(R.layout.activity_main)

        //增加按钮事件

        val btn = findViewById<Button>(R.id.button)

        btn.setOnClickListener {
            Log.i(TAG,"on click button .")
            val intent = Intent(this,SecondActivity::class.java)

            //传递参数
            intent.putExtra("name", "Jack")
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume.")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy.")
    }
}
