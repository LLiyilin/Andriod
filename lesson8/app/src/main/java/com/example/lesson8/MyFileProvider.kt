package com.example.lesson8

import androidx.core.content.FileProvider

class MyFileProvider : FileProvider(R.xml.file_paths) {

    companion object{
        val ACTION = "cn.edu.swu.cs.android.lesson8.FILE_PROVIDER"
    }


}