package com.tawktest.takehomeexam.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.data.db.entities.UserProfileTable
import com.tawktest.takehomeexam.model.UserProfileData

@Dao
interface UserProfileTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(data : UserProfileTable) : Long

    @Query(value = "SELECT * FROM UserProfileTable WHERE id =:id ")
    suspend fun getUserProfile(id : Int) : UserProfileTable?

    @Query(value = "SELECT * FROM UserProfileTable WHERE id =:id ")
    fun getUserProfileTest(id : Int) : UserProfileTable

    @Query("UPDATE UserProfileTable SET notes = :notes WHERE id =:id")
    suspend fun saveNotes(id : Int, notes : String) : Int


}