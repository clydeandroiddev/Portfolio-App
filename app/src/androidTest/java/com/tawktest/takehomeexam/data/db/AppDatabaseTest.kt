package com.tawktest.takehomeexam.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.data.db.dao.UserListTableDao
import com.tawktest.takehomeexam.data.db.dao.UserProfileTableDao
import com.tawktest.takehomeexam.data.db.entities.UserListTable
import com.tawktest.takehomeexam.data.db.entities.UserProfileTable
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase(){

    private lateinit var db: AppDatabase

    private lateinit var userlistdao : UserListTableDao
    private lateinit var userprofiledao : UserProfileTableDao

    @Before
    public override fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userlistdao = db.getUserListDao()
        userprofiledao = db.getUserProfileDao()
    }


    @After
    fun closeDB(){
        db.close()
    }

    // READ AND WRITE FOR USER LIST TABLE
    @Test
    fun writeAndReadUserList(): Unit = runBlocking{
        val insertUserLists = ArrayList<UserListTable>()
        val user1 = UserListTable(1,"mojombo",  "https://avatars.githubusercontent.com/u/1?v=4", "https://api.github.com/users/mojombo")
        val user2 = UserListTable(2,"defunkt",  "https://avatars.githubusercontent.com/u/2?v=4", "https://api.github.com/users/defunkt")
        insertUserLists.add(user1)
        insertUserLists.add(user2)
        userlistdao.insertUserListMultiple(insertUserLists)
        val userlist = userlistdao.getUserListTest()
        assertThat(userlist.size).isGreaterThan(0)
    }

    //INSERT, UPDATE AND SELECT FOR User Profile Table
    @Test
    fun writeAndReadUserProfile(): Unit = runBlocking {
        val profile1 = UserProfileTable(1, "mojombo","https://avatars.githubusercontent.com/u/1?v=4", "Tom Preston-Werner",  "@chatterbugapp, @redwoodjs, @preston-werner-ventures ","http://tom.preston-werner.com","San Francisco",62,62,22399,11,"2007-10-20T05:24:19Z","2021-03-18T22:50:53Z")
        val profile2 = UserProfileTable(2, "defunkt","https://avatars.githubusercontent.com/u/2?v=4", "Chris Wanstrath",  "@chatterbugapp, @redwoodjs, @preston-werner-ventures ","http://tom.preston-werner.com","San Francisco",62,62,22399,11,"2007-10-20T05:24:19Z","2021-03-18T22:50:53Z")
        userprofiledao.insertUserProfile(profile1)
        userprofiledao.insertUserProfile(profile2)
        val notes = "Test Note"
        val id = 1
        userprofiledao.saveNotes(id, notes)
        val userprofile = userprofiledao.getUserProfileTest(id)
        assertThat(userprofile.id == profile1.id && userprofile.notes.equals(notes)).isTrue()
    }

    //JOIN UserListTable and Profile Table For User Lists
    @Test
    fun readJoinUserListToUserProfile(): Unit = runBlocking {   val insertUserLists = ArrayList<UserListTable>()
        val user1 = UserListTable(1,"mojombo",  "https://avatars.githubusercontent.com/u/1?v=4", "https://api.github.com/users/mojombo")
        val user2 = UserListTable(2,"defunkt",  "https://avatars.githubusercontent.com/u/2?v=4", "https://api.github.com/users/defunkt")
        insertUserLists.add(user1)
        insertUserLists.add(user2)
        userlistdao.insertUserListMultiple(insertUserLists)
        val profile1 = UserProfileTable(1, "mojombo","https://avatars.githubusercontent.com/u/1?v=4", "Tom Preston-Werner",  "@chatterbugapp, @redwoodjs, @preston-werner-ventures ","http://tom.preston-werner.com","San Francisco",62,62,22399,11,"2007-10-20T05:24:19Z","2021-03-18T22:50:53Z")
        val profile2 = UserProfileTable(2, "defunkt","https://avatars.githubusercontent.com/u/2?v=4", "Chris Wanstrath",  "@chatterbugapp, @redwoodjs, @preston-werner-ventures ","http://tom.preston-werner.com","San Francisco",62,62,22399,11,"2007-10-20T05:24:19Z","2021-03-18T22:50:53Z")
        userprofiledao.insertUserProfile(profile1)
        userprofiledao.insertUserProfile(profile2)
        val notes = "Test Note"
        val id = 1
        userprofiledao.saveNotes(id, notes)
        val userlist = userlistdao.getUserListJoinTest()
        assertThat(userlist.size).isGreaterThan(0)
    }


}