package com.example.puremusic.bridge.db
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun login(username: String, password: String): User?

    @Insert
    fun register(user: User)
}
