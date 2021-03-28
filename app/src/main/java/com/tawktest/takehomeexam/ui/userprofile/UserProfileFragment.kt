package com.tawktest.takehomeexam.ui.userprofile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
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
import com.tawktest.takehomeexam.util.showCustomAlertDialog
import kotlinx.android.synthetic.main.user_profile_fragment.*
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

    var alertDialog : AlertDialog? = null

    var mView : View? = null

    var mLastTimeClick = System.currentTimeMillis();
    val CLICK_TIME_INTERVAL: Long = 1000

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
        mView = binding.root
        return mView
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                //Prevent multi taps
                val now = System.currentTimeMillis()
                if (now - mLastTimeClick < CLICK_TIME_INTERVAL) {
                    return false
                }
                mLastTimeClick = now
                (activity as MainActivity).onNavigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    private fun bindUI() = Coroutines.main {
        if(mView != null){
            viewModel.retrievedUserProfile(user_id, user_url).observe(viewLifecycleOwner, Observer {
                shimmer_user_profile.stopShimmerAnimation()
                shimmer_user_profile.visibility = View.GONE
                cl_primary_layout.visibility = View.VISIBLE
                binding.profile = it
                viewModel.user_id = user_id
                it.notes?.let { note ->
                    if(note.isNotEmpty()){
                        edit_note.setText(note)
                    }
                }
            })
        }

    }

    override fun onStarted() {
        shimmer_user_profile.startShimmerAnimation()
    }

    override fun onSuccess(data: String) {
    }

    override fun onFailure(title: String, message: String) {
        (activity as MainActivity).let{
            if(alertDialog == null){
                alertDialog = it.showCustomAlertDialog(title, message)
                alertDialog?.show()
            }else{
                if(alertDialog!!.isShowing)
                    alertDialog?.dismiss()

                alertDialog = it.showCustomAlertDialog(title, message)
                alertDialog?.show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onStart() {
        super.onStart()
    }

}