package com.tawktest.takehomeexam.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.data.db.AppDatabase
import com.tawktest.takehomeexam.data.db.entities.UserListTable
import com.tawktest.takehomeexam.data.db.entities.UserProfileTable
import com.tawktest.takehomeexam.data.network.ApiCalls
import com.tawktest.takehomeexam.data.network.SafeApiRequest
import com.tawktest.takehomeexam.data.preferences.PreferenceProvider
import com.tawktest.takehomeexam.model.UserProfileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private val MINIMUM_INTERVAL = 1

class UserRepository(
    private val api : ApiCalls,
    private val db : AppDatabase,
    private val prefs : PreferenceProvider
) : SafeApiRequest() {


    suspend fun getGithubUserProfile(url: String): UserProfileTable {
        return apiRequest { api.userprofile(url) }
    }

    suspend fun getGithubUserLists(id: Int): List<UserListTable> {
        return apiRequest { api.userlist(id) }
    }

    suspend fun saveUserProfile(profile: UserProfileTable) =
        db.getUserProfileDao().insertUserProfile(profile)

    suspend fun saveUserList(data: List<UserListTable>) {
        prefs.savelastSavedAt(LocalDateTime.now().toString())
        db.getUserListDao().insertUserListMultiple(data)
    }

    fun retrieveUserList() = db.getUserListDao().getUserList()

    fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
        return ChronoUnit.HOURS.between(savedAt, LocalDateTime.now()) > MINIMUM_INTERVAL
    }

    fun lastSavedAt() = prefs.getLastSavedAt()

    suspend fun retrieveUserProfile(id : Int) = db.getUserProfileDao().getUserProfile(id)

}