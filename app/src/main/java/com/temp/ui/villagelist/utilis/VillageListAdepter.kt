package com.temp.ui.villagelist.utilis

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.temp.R
import com.temp.databinding.ListVillageBinding
import com.temp.ui.addvillage.datamodel.AddVillage
import com.temp.ui.villagelist.datamodel.VilllageListData
import kotlinx.android.synthetic.main.list_village.view.*

class VillageListAdepter ()  : RecyclerView.Adapter<VillageListAdepter.MyViewHolder>() {

    private lateinit var mEventListener: EventListener

    private var data = mutableListOf<AddVillage>()
    lateinit var context: Context


    constructor(context: Context) : this() {
        this.context = context

    }

    fun setEventListener(eventListener: EventListener) {
        mEventListener = eventListener
    }


    interface EventListener {
        fun onItemClick(pos: Int, item: AddVillage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemBinding = DataBindingUtil.inflate<ListVillageBinding>(
                inflater,
                R.layout.list_village, parent, false
        )
        return MyViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return data.size

    }

    fun getItem(p: Int): AddVillage{
        return data[p]

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        try {

            holder.itemBinding.tvVillage.text = item.village

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        holder.itemBinding.root.setOnClickListener {
            mEventListener.onItemClick(position,item)
        }

    }

    fun addAll(mData: List<AddVillage>?) {
        data.clear()
        data.addAll(mData!!)
        notifyDataSetChanged()
    }


    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(internal var itemBinding: ListVillageBinding) : RecyclerView.ViewHolder(itemBinding.root)
}