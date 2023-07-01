package com.example.lesson6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class FileActivity : AppCompatActivity() {
    val TAG="FileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        findViewById<Button>(R.id.btn_apply).setOnClickListener {
            val txt=findViewById<EditText>(R.id.input).text
            writeToFile(txt)
        }

        val str = readFile("xiushanfeiyi.txt")
        findViewById<TextView>(R.id.txt).text=str
    }

    private fun readFile(name:String) : String{
        val ipt = openFileInput(name)
        val reader = BufferedReader(InputStreamReader(ipt))
        reader.use {
            val sb = StringBuffer()
            reader.forEachLine {
                sb.append(it)
            }
            Log.d(TAG,"read from file : ${name} : $sb")

            return sb.toString()
        }
    }

    //将数据存储进文件中
    private fun writeToFile(txt: Editable?) {

        txt?.apply {
            val str= this.toString()
            Log.d(TAG,str)

            val out= openFileOutput("xiushanfeiyi.txt", MODE_PRIVATE)
            val writer=BufferedWriter(OutputStreamWriter(out))
            writer.use{
                it.write(str)
                Log.i(TAG,"写完 shixianfeiyi.txt 完成")
            }
        }

    }
}