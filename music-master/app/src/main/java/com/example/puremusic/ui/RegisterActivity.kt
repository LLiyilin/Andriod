package com.example.puremusic.ui


import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.EditText

import androidx.room.Room
import com.example.puremusic.R
import com.example.puremusic.bridge.db.User
import com.example.puremusic.bridge.db.AppDatabase
import com.example.puremusic.ui.base.BaseActivity

class RegisterActivity : BaseActivity() {
    var username: EditText? = null
    var password: EditText? = null
    var password1:EditText? = null
    var register: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        findViews()
        findViewById<View>(R.id.exit).setOnClickListener { finish() }
        register!!.setOnClickListener(View.OnClickListener {
            val name:String = username!!.text.toString().trim { it <= ' ' }
            val pass:String = password!!.text.toString().trim { it <= ' ' }
            val pass1 = password1!!.text.toString().trim { it <= ' ' }
            if (name.isEmpty() || pass.isEmpty() || pass1.isEmpty()) {
                showShortToast("请完整填写好上面的信息！")
                return@OnClickListener
            }
            if (pass != pass1) {
                showShortToast("两次填写的密码不相同！")
                return@OnClickListener
            }

            val appDatabase = Room.databaseBuilder(this@RegisterActivity, AppDatabase::class.java, "user.db")
                .allowMainThreadQueries()
                .build()

            val userDao = appDatabase.userDao()
            val user = User()
            user.username = name
            user.password = pass
            userDao.register(user)
            showShortToast("注册成功")
            finish()
        })
    }

    private fun findViews() {
        username = findViewById(R.id.input1)
        password = findViewById(R.id.input2)
        password1 = findViewById(R.id.input3)
        register = findViewById(R.id.login_button)
    }
}