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

            //INSTANCE가 null이면 초기화
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
