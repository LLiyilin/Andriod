package com.example.puremusic.bridge.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User {
    @PrimaryKey(autoGenerate = true) var id:Int?=0
    var username: String? = null
    var password: String? = null
}