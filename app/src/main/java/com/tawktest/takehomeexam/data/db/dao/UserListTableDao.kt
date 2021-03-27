package com.tawktest.takehomeexam.data.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.data.db.entities.UserListTable

@Dao
interface UserListTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserList(data : UserListTable) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserListMultiple(data: List<UserListTable>)

    @Query(value = "SELECT list.id, list.login, list.avatar_url, list.url, profile.notes FROM UserListTable AS list LEFT JOIN  UserProfileTable as profile ON list.id = profile.id")
    fun getUserList() : LiveData<List<UserListData>>

    @Query(value = "SELECT * FROM UserListTable")
    fun getUserListTest() : LiveData<List<UserListTable>>

    @Query(value = "SELECT list.id, list.login, list.avatar_url, list.url, profile.notes FROM UserListTable AS list LEFT JOIN  UserProfileTable as profile ON list.id = profile.id")
    fun getUserListJoinTest() : List<UserListTable>
}