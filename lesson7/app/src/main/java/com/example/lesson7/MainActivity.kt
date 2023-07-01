package com.example.lesson7
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    val PHONE_PER_CODE = 1024

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launcher = registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                call()
            } else {
                Toast.makeText(this, "您没有统一授权打电话！", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.btn_call).setOnClickListener {
            val phonePer = "android.permission.CALL_PHONE"
            if (ActivityCompat.checkSelfPermission(this, phonePer)
                == PackageManager.PERMISSION_GRANTED
            ) {
                call()
            } else {
                launcher.launch(phonePer)
                requestPermissions(arrayOf(phonePer),PHONE_PER_CODE)

            }

        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == PHONE_PER_CODE){
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                call()
//            }else{
//                Toast.makeText(this,"您没有统一授权打电话！",Toast.LENGTH_LONG).show()
//            }
//        }
//    }

    fun call() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:10086")
        startActivity(intent)
    }
}