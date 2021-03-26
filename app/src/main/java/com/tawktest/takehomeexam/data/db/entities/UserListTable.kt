package com.tawktest.takehomeexam.data.db.entities

import androidx.room.Entity

@Entity
data class UserListTable(
    val login : String? = null,
    val id : Int? = 0,
    val avatar_url : String? = null,
    val url : String? = null
)
