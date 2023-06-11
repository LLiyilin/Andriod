package com.example.myapplication1

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    val TAG = "SecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG,"$TAG onCreate .")
        this.setContentView(R.layout.activity_second)//跳转

        //接受传递数据
        //val intent=getIntent()
        val name = intent.getStringExtra("name")
        val txtName = findViewById<TextView>(R.id.text_name)

//txtName.setText(name)
        txtName.text = name
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG,"$TAG onStart .")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG,"$TAG onResume .")
    }

    override fun onPause(){
        super.onPause()
        Log.d(TAG,"$TAG onPause .")
    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG,"$TAG onStop .")
    }

    override fun onRestart(){
        super.onRestart()
        Log.d(TAG,"$TAG onRestart .")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"$TAG onDestroy .")
    }


}