package com.tawktest.takehomeexam.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserListTable(
    @PrimaryKey(autoGenerate = false)
    val id : Int = 0,
    val login : String = "",
    val avatar_url : String = "",
    val url : String = "",
    val notes : String = ""
)
