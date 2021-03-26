package com.tawktest.takehomeexam.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.model.UserProfileData

@Dao
interface UserProfileTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfil(data : UserProfileData) : Long

    @Query(value = "SELECT * FROM UserProfileTable WHERE id =:id ")
    fun getUserProfile(id : Int) : LiveData<UserProfileData>


}