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

class TodoActivity : AppCompatActivity() {
    val TAG = "FileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        findViewById<Button>(R.id.btn_apply).setOnClickListener {
            val txt = findViewById<EditText>(R.id.input).text
            // writeToFile(txt)
//            writeToSp("todo",txt.toString())
            writeToDb(txt.toString())
        }

//        val str = readFile("swu.txt")
//        val str = readSp("swu","todo")
        readDb()
    }

    private fun getDbHelper():MyDbHelper{
        return MyDbHelper(this, "todo.db", 2)
    }

    private fun readDb() {
        val helper = getDbHelper()
        val db = helper.readableDatabase

        val cursor = db.rawQuery("select * from Todo", null)

        val sb = StringBuffer()
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(Math.max(cursor.getColumnIndex("id"), 0))
                    val content = cursor.getString(Math.max(cursor.getColumnIndex("content"), 0))
                    val down = cursor.getInt(Math.max(cursor.getColumnIndex("down"), 0))
                    val time = cursor.getInt(Math.max(cursor.getColumnIndex("create_time"), 0))


                    sb.append("id=${id},content=${content},down=${down} ,time=${time} \n")

                } while (cursor.moveToNext())
            }
        }

        findViewById<TextView>(R.id.txt).text = sb.toString()
    }


    private fun writeToDb(value: String) {
        val helper = getDbHelper()
        val db = helper.writableDatabase
        db.execSQL("insert into Todo(content,create_time) values (?,?)", arrayOf(value,System.currentTimeMillis() / 1000))

        Log.i(TAG, "写入数据库成功~")

        readDb()
    }

    private fun readSp(name: String, key: String): String? {
        val sp = getSharedPreferences(name, MODE_PRIVATE)
        return sp.getString(key, "")
    }

    private fun writeToSp(key: String, value: String) {
        val sp = getSharedPreferences("swu", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(key, value)
        editor.apply()
        Log.i(TAG, "写入SP成功")
    }

    private fun readFile(name: String): String {
        val ipt = openFileInput(name)
        val reader = BufferedReader(InputStreamReader(ipt))
        reader.use {
            val sb = StringBuffer()
            reader.forEachLine {
                sb.append(it)
            }
            Log.d(TAG, "read from file : ${name} : $sb")

            return sb.toString()
        }

    }

    private fun writeToFile(txt: Editable?) {

        txt?.apply {
            val str = this.toString()
            Log.d(TAG, str)

            val out = openFileOutput("swu.txt", MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(out))
            writer.use {
                it.write(str)
                Log.i(TAG, "写完 swu.txt 完成")
            }
        }

    }
}