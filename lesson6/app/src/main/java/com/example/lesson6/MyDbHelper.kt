package com.example.lesson6

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.security.PrivateKey

class MyDbHelper(
    context: Context?,
    name: String?,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {

    val TAG = "MyDbHelper"

    val createTable = "create table Todo(" +
            "id integer primary key autoincrement," +
            "content text ," +
            "down integer default 0)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable)
        Log.i(TAG, "初始化数据库")
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, newbe: Int) {
        Log.i(TAG, "数据库更新：${old} -> ${newbe}")

        for (i in old..newbe) {
            Log.w(TAG, "version : $i")

            when (i) {
                2 -> {
                    fixVersion2(db)
                }
                3 -> {
                    fixVersion3(db)
                }
                4 -> {
                    fixVersion4(db)
                }
            }
        }
    }

    private fun fixVersion4(db: SQLiteDatabase?) {
        Log.w(TAG, "更新版本4")
    }

    private fun fixVersion3(db: SQLiteDatabase?) {
        Log.w(TAG, "更新版本3")
    }

    private fun fixVersion2(db: SQLiteDatabase?) {
        db?.execSQL("alter table Todo add column create_time INTEGER")
        Log.w(TAG, "更新版本2")
    }
}