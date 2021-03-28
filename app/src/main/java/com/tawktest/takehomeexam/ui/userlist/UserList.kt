package com.tawktest.takehomeexam.ui.userlist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawktest.takehomeexam.R
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.model.ConnectionData
import com.tawktest.takehomeexam.ui.MainActivity
import com.tawktest.takehomeexam.ui.MainActivityViewModel
import com.tawktest.takehomeexam.util.*
import com.tawktest.takehomeexam.util.Constants.Companion.VIEW_TYPE_ITEM
import com.tawktest.takehomeexam.util.Constants.Companion.VIEW_TYPE_LOADING
import kotlinx.android.synthetic.main.user_list_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class UserList : Fragment(), KodeinAware, GlobalListener, UserListAdapter.AdapterListener {



    override val kodein by kodein()
    private val factory: UserListViewModelFactory by instance()


    private lateinit var viewModel: UserListViewModel
    lateinit var scrollListener: RecyclerViewLoadMoreScroll

    var mAdapter : UserListAdapter? = null
    var loadMoreUserList : ArrayList<UserListData?>? = null
    var isLoadedFirstTime = true;
    var searchView : SearchView? = null

    var listIsEmpty = true


    var mLastTimeClick = System.currentTimeMillis();
    val CLICK_TIME_INTERVAL: Long = 1000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.nav_userlist_title)


        return inflater.inflate(R.layout.user_list_fragment, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val mSearchMenuItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = mSearchMenuItem.getActionView() as SearchView
        searchView?.isIconified = false
        searchView?.clearFocus()

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                mAdapter?.filter?.filter(query)
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
        val shareViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        shareViewModel.toLoad.observe( this, {
            if(it && listIsEmpty){
                txt_no_records.visibility = View.GONE
                shimmer_user_list.visibility = View.VISIBLE
                shimmer_user_list.startShimmerAnimation()
            }
        }

        )
        viewModel.globalListener = this

        shimmer_user_list.startShimmerAnimation()
        bindUI()
    }



    private fun bindUI() = Coroutines.main{
        viewModel.retrievedUserLists().observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                listIsEmpty = false
                searchView?.isEnabled = true
                Handler(Looper.getMainLooper()).postDelayed({
                    shimmer_user_list.stopShimmerAnimation()
                    shimmer_user_list.visibility = View.GONE
                    txt_no_records.visibility = View.GONE
                    rv_user_list.visibility = View.VISIBLE
                }, 1000)
                initRecyclerView(list)
            } else {
                listIsEmpty = true
                searchView?.isEnabled = false
                Handler(Looper.getMainLooper()).postDelayed({
                    shimmer_user_list.stopShimmerAnimation()
                    shimmer_user_list.visibility = View.GONE
                    txt_no_records.visibility = View.VISIBLE
                }, 2000)

            }

        })
    }

    private fun initRecyclerView(data: List<UserListData>?) {
        mAdapter = UserListAdapter(ArrayList(data), this@UserList)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val gridLayoutManager = GridLayoutManager(context, 1)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mAdapter?.getItemViewType(position)) {
                    VIEW_TYPE_ITEM -> 1
                    VIEW_TYPE_LOADING -> 1 //number of columns of the grid
                    else -> -1
                }
            }
        }
        rv_user_list.apply {
            layoutManager = gridLayoutManager
            adapter = mAdapter
            setHasFixedSize(true)
        }
        mAdapter?.notifyDataSetChanged()

        scrollListener = RecyclerViewLoadMoreScroll(linearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                Coroutines.main {
                    LoadMoreData()
                }
            }
        })
        rv_user_list.addOnScrollListener(scrollListener)
    }


    override fun onStarted() {
        //progress_bar.show()
    }

    override fun onSuccess(data: String) {
        //progress_bar.hide()
    }

    override fun onFailure(title: String, message: String) {
        //progress_bar.hide()
    }



    override fun onClickItem(id: Int, url: String) {
        //Prevent MultiTaps
        val now = System.currentTimeMillis()
        if (now - mLastTimeClick < CLICK_TIME_INTERVAL) {
            return
        }
        mLastTimeClick = now

        val bundle = Bundle()
        bundle.putInt(Constants.KEY_USER_ID, id)
        bundle.putString(Constants.KEY_USER_URL, url)
        (activity as MainActivity).findNavController(R.id.nav_container).navigate(
            R.id.action_userList_to_userProfileFragment,
            bundle
        )
    }

    private suspend fun LoadMoreData() {
        //Add the Loading View
        mAdapter?.addLoadingView()
        loadMoreUserList = ArrayList()
        val lastID = mAdapter?.userlistFilter?.last()?.id

        Handler(Looper.getMainLooper()).postDelayed({
            Coroutines.main {
                lastID?.let {
                    viewModel.loadMoreUserList(lastID)?.observe(this, Observer {
                        it?.let {
                            mAdapter?.removeLoadingView()
                            mAdapter?.addData(ArrayList(it))
                            scrollListener.setLoaded()
                            rv_user_list.post {
                                mAdapter?.notifyDataSetChanged()
                            }
                        }

                    })
                }
            }
        }, 3000)
    }

}