package com.example.oceanit.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao?

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDBInstance(context: Context): AppDatabase? {

            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "User_data"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }

}
