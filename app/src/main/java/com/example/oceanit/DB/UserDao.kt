package com.example.oceanit.DB

import androidx.room.*


@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id : Int): User

    @Query("SELECT * FROM user")
    fun getAll() : List<User>

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM user")
    fun deleteAllUsers()

}
