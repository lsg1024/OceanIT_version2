package com.example.oceanit.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var loginId: String,
    var loginPw: String
)