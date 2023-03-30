package com.example.oceanit.DB

import androidx.room.*


@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = :id")
    fun getAll(id : Int): User

    @Insert
    fun insert(user: User)

    @Delete
    fun userDelete(user: User?)

    @Update
    fun userUpdate(user: User?)
}
