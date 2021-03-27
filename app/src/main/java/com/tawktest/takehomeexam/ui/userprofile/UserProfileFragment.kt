package com.tawktest.takehomeexam.ui.userprofile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.tawktest.takehomeexam.R
import com.tawktest.takehomeexam.databinding.UserProfileFragmentBinding
import com.tawktest.takehomeexam.ui.MainActivity
import com.tawktest.takehomeexam.ui.userlist.UserListViewModelFactory
import com.tawktest.takehomeexam.util.Constants
import com.tawktest.takehomeexam.util.Coroutines
import com.tawktest.takehomeexam.util.GlobalListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class UserProfileFragment : Fragment(), KodeinAware, GlobalListener {


    private lateinit var viewModel: UserProfileViewModel

    override val kodein by kodein()
    private val factory: UserProfileViewModelFactory by instance()

    lateinit var binding: UserProfileFragmentBinding

    var user_id = 0
    var user_url = ""
    var toolbar : ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        toolbar = (activity as MainActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = getString(R.string.nav_user_profile_title)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.user_profile_fragment,
            container,
            false
        )

        arguments?.let {
            user_id = it.getInt(Constants.KEY_USER_ID)
            it.getString(Constants.KEY_USER_URL)?.let {
                user_url = it
            }

        }

        viewModel = ViewModelProvider(this,factory).get(UserProfileViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.globalListener = this

        return binding.root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                (activity as MainActivity).onNavigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    private fun bindUI() = Coroutines.main {
        if(view != null){
            viewModel.retrievedUserProfile(user_id, user_url).observe(viewLifecycleOwner, Observer {
                binding.profile = it
            })
        }

    }

    override fun onStarted() {
    }

    override fun onSuccess(data: String) {
    }

    override fun onFailure(title: String, message: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar?.setDisplayHomeAsUpEnabled(false)
    }

}