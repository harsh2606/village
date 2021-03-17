package com.temp.ui.mainmember.utilis

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.temp.R
import com.temp.databinding.ListMainmemberBinding
import com.temp.ui.mainmember.datamodel.AddMemberData

class MainMemberAdepter ()  : RecyclerView.Adapter<MainMemberAdepter.MyViewHolder>() {

    private lateinit var mEventListener: EventListener

    private var data = mutableListOf<AddMemberData>()
    lateinit var context: Context


    constructor(context: Context) : this() {
        this.context = context

    }

    fun setEventListener(eventListener: EventListener) {
        mEventListener = eventListener
    }


    interface EventListener {
        fun onItemClick(pos: Int, item:AddMemberData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemBinding = DataBindingUtil.inflate<ListMainmemberBinding>(
            inflater,
            R.layout.list_mainmember, parent, false
        )
        return MyViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return data.size

    }

    fun getItem(p: Int):AddMemberData {
        return data[p]

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        try {

            holder.itemBinding.tvName.text = item.name
            holder.itemBinding.tvNumber.text = item.number

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        holder.itemBinding.root.setOnClickListener {
            mEventListener.onItemClick(position,item)
        }

    }

    fun addAll(mData: List<AddMemberData>?) {
        data.clear()
        data.addAll(mData!!)
        notifyDataSetChanged()
    }


    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(internal var itemBinding: ListMainmemberBinding) : RecyclerView.ViewHolder(itemBinding.root)
}