package com.tawktest.takehomeexam.model

data class UserProfileData(
    val login : String? = null,
    val id : Int? = 0,
    val avatar_url : String? = null,
    val name : String? = null,
    val company : String? = null,
    val blog : String? = null,
    val location : String? = null,
    val public_repos : Int? = 0,
    val public_gist : Int? = 0,
    val followers: Int? = 0,
    val following : Int? = 0,
    val created_at : String? = null,
    val update_at : String? = null
)