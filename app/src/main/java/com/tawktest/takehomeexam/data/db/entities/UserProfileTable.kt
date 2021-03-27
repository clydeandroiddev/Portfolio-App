package com.tawktest.takehomeexam.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserProfileTable(
    @PrimaryKey(autoGenerate = false)
    val id : Int = 0,
    val login : String? = "",
    val avatar_url : String? = "",
    val name : String? = "",
    val company : String? = "",
    val blog : String? = "",
    val location : String? = "",
    val public_repos : Int = 0,
    val public_gist : Int = 0,
    val followers: Int = 0,
    val following : Int = 0,
    val created_at : String? = "",
    val update_at : String? = "",
    val notes : String? = ""
)
