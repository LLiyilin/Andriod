package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener

class LoginActivity : AppCompatActivity() {
    var username:String? = null
    var password:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<EditText>(R.id.username).addTextChangedListener {
            username = it.toString()
        }

        findViewById<EditText>(R.id.password).addTextChangedListener {
            password = it.toString()
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            if(username != null && password != null) {
                //校验
                if(username == "admin" && password == "123456") {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("user", username)
                    startActivity(intent)
                    finish()
                } else {
                    showError()
                }
            } else {
                showError()
            }
        }
    }
    private fun showError() {
        Toast.makeText(this, "请输入正确的用户密码", Toast.LENGTH_LONG).show()
    }
}