package com.tawktest.takehomeexam.ui.userlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tawktest.takehomeexam.R
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.util.Constants
import kotlinx.android.synthetic.main.item_userlist.view.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class UserListAdapter(val userlist: ArrayList<UserListData?>, val adapterListener: AdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    lateinit var mcontext: Context

    var userlistFiler = ArrayList<UserListData?>()

    init {
        userlistFiler = userlist
    }



    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       mcontext = parent.context
        return  if(viewType == Constants.VIEW_TYPE_ITEM){
            val view = LayoutInflater.from(mcontext).inflate(R.layout.item_userlist, parent, false)
            ItemViewHolder(view)
        }else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            Constants.VIEW_TYPE_ITEM ->{
                bindData(holder, userlistFiler[position])
            }
        }
    }

    private fun bindData(holder: RecyclerView.ViewHolder, UserListData: UserListData?) {
        UserListData?.let { data ->
            holder.itemView.apply {
                data.avatar_url?.let{
                    if(it.isNotEmpty()){
                        Picasso.get().load(data.avatar_url).into(img_avatar)
                    }
                }



                    txt_username.text = data.login
                    txt_details.text = "User ID: ".plus(data.id)



                    if(!data.notes.isNullOrEmpty()){
                        img_note.visibility = View.VISIBLE
                    }else {
                        img_note.visibility = View.GONE
                    }

                    setOnClickListener {
                        adapterListener.onClickItem(data.id!!, data.url!!)
                    }

            }
        }
    }

    override fun getItemCount(): Int = userlistFiler.size

    override fun getFilter(): Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    userlistFiler = userlist
                } else {
                    val resultList = ArrayList<UserListData?>()
                    for(row in userlist){
                        if(row?.login?.toLowerCase(Locale.ROOT)?.contains(charSearch.toLowerCase(Locale.ROOT))!!){
                            resultList.add(row)
                        }
                    }
                    userlistFiler = resultList

                }
                val filterResults = FilterResults()
                filterResults.values = userlistFiler
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                userlistFiler = results?.values as ArrayList<UserListData?>
                notifyDataSetChanged()
            }
        }
    }



    interface AdapterListener {
        fun onClickItem(id: Int, url: String)
    }

}

