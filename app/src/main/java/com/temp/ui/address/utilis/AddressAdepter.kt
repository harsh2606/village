package com.temp.ui.address.utilis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.temp.R
import com.temp.databinding.ListAddressBinding
import com.temp.databinding.ListMainmemberBinding
import com.temp.ui.address.datamodel.AddressData
import com.temp.ui.mainmember.datamodel.MainMemberData
import com.temp.ui.mainmember.utilis.MainMemberAdepter

class AddressAdepter ()  : RecyclerView.Adapter<AddressAdepter.MyViewHolder>() {

    private lateinit var mEventListener: EventListener

    private var data = mutableListOf<AddressData>()
    lateinit var context: Context


    constructor(context: Context) : this() {
        this.context = context

    }

    fun setEventListener(eventListener: EventListener) {
        mEventListener = eventListener
    }


    interface EventListener {
        fun onItemClick(pos: Int, item: AddressData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemBinding = DataBindingUtil.inflate<ListAddressBinding>(
            inflater,
            R.layout.list_address, parent, false
        )
        return MyViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return data.size

    }

    fun getItem(p: Int): AddressData {
        return data[p]

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        try {



            if(data.get(position).relation!!.isNullOrEmpty().not()){
                holder.itemBinding.tvRNames.visibility = View.VISIBLE
            }else{
                holder.itemBinding.tvRNames.visibility = View.GONE
            }

            if(data.get(position).name!!.isNullOrEmpty().not()){
                holder.itemBinding.tvName.visibility = View.VISIBLE
            }else{
                holder.itemBinding.tvName.visibility = View.GONE
            }

            if(data.get(position).business!!.isNullOrEmpty().not()){
                holder.itemBinding.tvBusiness1.visibility = View.VISIBLE
            }else{
                holder.itemBinding.tvBusiness1.visibility = View.GONE
            }

            if(data.get(position).dob!!.isNullOrEmpty().not()){
                holder.itemBinding.tvDob1.visibility = View.VISIBLE
            }else{
                holder.itemBinding.tvDob1.visibility = View.GONE
            }

            if(data.get(position).study!!.isNullOrEmpty().not()){
                holder.itemBinding.tvStudy1.visibility = View.VISIBLE
            }else{
                holder.itemBinding.tvStudy1.visibility = View.GONE
            }

            if(data.get(position).number!!.isNullOrEmpty().not()){
                holder.itemBinding.tvNumber1.visibility = View.VISIBLE
            }else{
                holder.itemBinding.tvNumber1.visibility = View.GONE
            }

            if (data.isNullOrEmpty().not()) {

                holder.itemBinding.tvBusiness.visibility = View.VISIBLE
                holder.itemBinding.tvDob.visibility = View.VISIBLE
                holder.itemBinding.tvStudy.visibility = View.VISIBLE
                holder.itemBinding.tvName.visibility = View.VISIBLE
                holder.itemBinding.tvNumber.visibility = View.VISIBLE
            } else {

                holder.itemBinding.tvBusiness.visibility = View.GONE
                holder.itemBinding.tvDob.visibility = View.GONE
                holder.itemBinding.tvStudy.visibility = View.GONE
                holder.itemBinding.tvName.visibility = View.GONE
                holder.itemBinding.tvNumber.visibility = View.GONE
            }

            holder.itemBinding.tvRName.text = item.relation
            holder.itemBinding.tvBusiness.text = item.business
            holder.itemBinding.tvDob.text = item.dob
            holder.itemBinding.tvStudy.text = item.study
            holder.itemBinding.tvName.text = item.name
            holder.itemBinding.tvNumber.text = item.number

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        holder.itemBinding.root.setOnClickListener {
            mEventListener.onItemClick(position,item)
        }

    }

    fun addAll(mData: List<AddressData>?) {
        data.clear()
        data.addAll(mData!!)
        notifyDataSetChanged()
    }


    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(internal var itemBinding: ListAddressBinding) : RecyclerView.ViewHolder(itemBinding.root)
}