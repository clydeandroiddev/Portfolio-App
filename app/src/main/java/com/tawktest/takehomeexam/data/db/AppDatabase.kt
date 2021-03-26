package com.tawktest.takehomeexam.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tawktest.takehomeexam.data.db.dao.UserListTableDao
import com.tawktest.takehomeexam.data.db.dao.UserProfileTableDao
import com.tawktest.takehomeexam.data.db.entities.UserListTable
import com.tawktest.takehomeexam.data.db.entities.UserProfileTable


@Database(
    entities = [UserListTable::class, UserProfileTable::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getUserListDao() : UserListTableDao
    abstract fun getUserProfileDao() : UserProfileTableDao

    companion object {
        @Volatile
        private var instance : AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "GithubUsers.db"
            ).build()
    }

}