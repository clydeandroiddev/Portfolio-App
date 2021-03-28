package com.tawktest.takehomeexam.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.tawktest.takehomeexam.R
import com.tawktest.takehomeexam.model.ConnectionData
import com.tawktest.takehomeexam.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware, GlobalListener {

    override val kodein by kodein()

    private val factory : MainActivityViewModelFactory by instance()

    var alertDialog : AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)
        viewModel.globalListener = this
        if(!InternetConnectionUtil.isInternetOn()){
            if(viewModel.lastSaveAt() == null){
                viewModel.globalListener?.onFailure("Warning", "Please make sure you have internet connection when first time running the app.")
            }
        }
        setupInternConnectivity(viewModel)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_container).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun setupInternConnectivity(viewModel: MainActivityViewModel) {
        InternetConnectionUtil.observe(this, Observer {
            status ->
            if(status){
                viewModel.fetchUserList()
            }
        })
       /* val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, object : Observer<ConnectionData> {
            override fun onChanged(@Nullable connection: ConnectionData) {
                when(connection.type){
                    Constants.CONNECTIVITY_TYPE_3 -> {
                        viewModel.fetchUserList()
                    }
                    Constants.CONNECTIVITY_TYPE_2 -> {

                    }
                    Constants.CONNECTIVITY_TYPE_1 -> {

                    }
                }
            }
        })*/
    }

    override fun onStarted() {
    }

    override fun onSuccess(data: String) {
        alertDialog?.let {
            if(it.isShowing){
                it.dismiss()
            }
        }
    }

    override fun onFailure(title: String, message: String) {
        if(alertDialog == null){
            alertDialog = this.showCustomAlertDialog(title, message)
            alertDialog?.show()
        }else{
            if(alertDialog!!.isShowing)
                alertDialog?.dismiss()

            alertDialog = this.showCustomAlertDialog(title, message)
            alertDialog?.show()
        }

    }

}