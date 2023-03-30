package com.example.oceanit.DB

import androidx.room.*


@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = :id")
    fun getAll(id : Int): User

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM user")
    fun deleteAllUsers()

    @Update
    fun userUpdate(user: User?)
}
