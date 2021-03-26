package com.tawktest.takehomeexam.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawktest.takehomeexam.UserListData

@Dao
interface UserListTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserList(data : UserListData) : Long

    @Query(value = "SELECT * FROM UserListTable")
    fun getUserList() : LiveData<List<UserListData>>
}