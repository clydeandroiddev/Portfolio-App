package com.tawktest.takehomeexam.ui.userlist

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.os.Handler
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

    var userlistFilter = ArrayList<UserListData?>()

    init {
        userlistFilter = userlist
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
            Constants.VIEW_TYPE_ITEM -> {
                bindData(holder, userlistFilter[position], position)
            }
        }
    }

    private fun bindData(
        holder: RecyclerView.ViewHolder,
        UserListData: UserListData?,
        position: Int
    ) {
        UserListData?.let { data ->
            holder.itemView.apply {
                data.avatar_url?.let {
                    if (it.isNotEmpty()) {

                        Picasso.get().load(data.avatar_url).into(img_avatar)
                        if(position != 0 && position % 3 == 0){
                            val NEGATIVE = floatArrayOf(
                                -1.0f,
                                0f,
                                0f,
                                0f,
                                255f,
                                0f,
                                -1.0f,
                                0f,
                                0f,
                                255f,
                                0f,
                                0f,
                                -1.0f,
                                0f,
                                255f,
                                0f,
                                0f,
                                0f,
                                1.0f,
                                0f
                            )
                            img_avatar.setColorFilter(ColorMatrixColorFilter(NEGATIVE))
                        }else {
                            img_avatar.setColorFilter(null)
                        }
                    }
                }




                txt_username.text = data.login
                txt_details.text = "User ID: ".plus(data.id)



                if (!data.notes.isNullOrEmpty()) {
                    img_note.visibility = View.VISIBLE
                } else {
                    img_note.visibility = View.GONE
                }

                setOnClickListener {
                    adapterListener.onClickItem(data.id!!, data.url!!)
                }

            }
        }
    }

    fun getItemAtPosition(position: Int): UserListData? {
        return userlistFilter[position]
    }

    override fun getItemCount(): Int = userlistFilter.size

    override fun getFilter(): Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    userlistFilter = userlist
                } else {
                    val resultList = ArrayList<UserListData?>()
                    for(row in userlist){
                        if(row?.login?.toLowerCase(Locale.ROOT)?.contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )!!){
                            resultList.add(row)
                        }
                    }
                    userlistFilter = resultList

                }
                val filterResults = FilterResults()
                filterResults.values = userlistFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                userlistFilter = results?.values as ArrayList<UserListData?>
                notifyDataSetChanged()
            }
        }
    }

    fun addLoadingView() {
        //Add loading item
        Handler().post {
            userlistFilter.add(null)
            notifyItemInserted(userlistFilter.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (userlistFilter.size != 0) {
            userlistFilter.removeAt(userlistFilter.size - 1)
            notifyItemRemoved(userlistFilter.size)
        }
    }

    fun addData(dataViews: ArrayList<UserListData>) {
        this.userlistFilter.addAll(dataViews)
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return if (userlistFilter[position] == null) {
            Constants.VIEW_TYPE_LOADING
        } else {
            Constants.VIEW_TYPE_ITEM
        }
    }



    interface AdapterListener {
        fun onClickItem(id: Int, url: String)
    }

}

