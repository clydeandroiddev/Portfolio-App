package com.tawktest.takehomeexam.ui.userlist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawktest.takehomeexam.R
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.ui.MainActivity
import com.tawktest.takehomeexam.util.*
import kotlinx.android.synthetic.main.user_list_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class UserList : Fragment(), KodeinAware, GlobalListener, UserListAdapter.AdapterListener {



    override val kodein by kodein()
    private val factory: UserListViewModelFactory by instance()


    private lateinit var viewModel: UserListViewModel

    var mAdapter : UserListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.nav_userlist_title)


        return inflater.inflate(R.layout.user_list_fragment, container, false)
    }
    var searchView : SearchView? = null
    override fun onPrepareOptionsMenu(menu: Menu) {
        val mSearchMenuItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = mSearchMenuItem.getActionView() as SearchView
        searchView?.isIconified = false
        searchView?.clearFocus()

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                mAdapter!!.filter.filter(query)
                return false
            }
        })
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(UserListViewModel::class.java)
        viewModel.globalListener = this
        viewModel.fetchUserList()
        bindUI()
    }

    private fun bindUI() = Coroutines.main{
        viewModel.retrievedUserLists().observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                initRecyclerView(list)
            }

        })
    }

    private fun initRecyclerView(data: List<UserListData>?) {
        mAdapter = UserListAdapter(ArrayList(data), this@UserList)

        rv_user_list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
            setHasFixedSize(true)
        }
        mAdapter?.notifyDataSetChanged()
    }


    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(data: String) {
        progress_bar.hide()
    }

    override fun onFailure(title: String, message: String) {
        progress_bar.hide()
    }

    override fun onClickItem(id: Int, url: String) {
        val bundle = Bundle()
        bundle.putInt(Constants.KEY_USER_ID, id)
        bundle.putString(Constants.KEY_USER_URL, url)
        (activity as MainActivity).findNavController(R.id.nav_container).navigate(R.id.action_userList_to_userProfileFragment,bundle)
    }

}