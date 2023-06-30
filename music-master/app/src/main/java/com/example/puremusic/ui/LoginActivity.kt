package com.example.puremusic.ui

import android.content.Intent
import android.content.SharedPreferences

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View

import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

import androidx.room.Room
import com.example.puremusic.R
import com.example.puremusic.bridge.db.AppDatabase
import com.example.puremusic.ui.base.BaseActivity

class LoginActivity : BaseActivity(), View.OnClickListener  {
    var button: Button? = null
    var edit1: EditText? = null
    var edit2:EditText? = null
    var checkbox: CheckBox? = null
    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<View>(R.id.exit).setOnClickListener { finish() }
        findViewById<View>(R.id.reg).setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        button = findViewById(R.id.login_button)
        edit1 = findViewById(R.id.input1)
        edit2 = findViewById(R.id.input2)
        checkbox = findViewById(R.id.remember_button)
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        val isRemember = pref?.getBoolean("rem", false) //用于给是否保存密码赋值


        if (isRemember == true) {
            //将账号和密码设置到文本框中
            val account = pref?.getString("account", "")
            val password = pref?.getString("password", "")
            edit1?.setText(account)
            edit2?.setText(password)
            checkbox?.setChecked(true)
        }
        button?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val account = edit1!!.text.toString().trim { it <= ' ' }
        val password = edit2!!.text.toString().trim { it <= ' ' }
        if (account.isEmpty() || password.isEmpty()) {
            showShortToast("请填写账号和密码")
            return
        }
        val appDatabase = Room.databaseBuilder(this@LoginActivity, AppDatabase::class.java, "user.db")
            .allowMainThreadQueries()
            .build()
        val userDao = appDatabase.userDao()
        val user = userDao.login(account, password)
        if (user != null) {
            editor = pref!!.edit()
            if (checkbox!!.isChecked) {
                editor?.putBoolean("rem", true)
                editor?.putString("account", account)
                editor?.putString("password", password)
            } else {
                editor?.clear()
            }
            editor?.commit()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            showShortToast("账号或用户名错误")
        }
    }
}