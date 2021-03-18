package com.temp.ui.address.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.firebase.messaging.Constants.TAG
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityAdressBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.addmaindetail.view.AddMainDetailActivity
import com.temp.ui.address.datamodel.AddressData
import com.temp.ui.address.utilis.AddressAdepter
import com.temp.ui.mainmember.datamodel.AddMemberData

class AddressViewModel (application: Application) : BaseViewModel(application) {
    private lateinit var binder: ActivityAdressBinding
    private lateinit var mContext: Context
    lateinit var addressAdepter: AddressAdepter
    lateinit var  addressData: AddressData
    var address = mutableListOf<AddressData>()
    var id: AddMemberData? = null


    fun setBinder(binder:ActivityAdressBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topbar.topBarClickListener = SlideMenuClickListener()
        this.binder.topbar.isTextShow = true
        this.binder.topbar.isBackShow = true
        this.binder.topbar.tvTitleText.text = (mContext as Activity).getString(R.string.mainmember_list)
       addressData=AddressData()
        init()
    }

    private fun init() {
        id = (mContext as Activity).intent.extras?.getSerializable("id") as AddMemberData?
         binder.tvAddress.setText(id!!.address)
        this.binder.topbar.tvTitleText.setText(id!!.name)

        addressAdepter = AddressAdepter(mContext)
        binder.rvAddressList.adapter =addressAdepter
        addressAdepter.setEventListener(object : AddressAdepter.EventListener {
            override fun onItemClick(pos: Int, item: AddressData) {




            }
        })


    }

    fun onResume() {
        getAddress()
    }




    private fun getAddress1() {
        try {

            Debug.e("villageID",id?.id)
            showDialog("",mContext as Activity)


            db!!.collection(FirestoreTable.Address).whereEqualTo("mainmemberid",id?.id!!.trim()).get()
                    .addOnSuccessListener{ documents ->
                        if (documents != null && documents.isEmpty.not()) {

                            val item = documents.toObjects(AddressData::class.java)
                            addressAdepter.addAll(item)
                            Debug.e("Get All Data Successfully"+item.toString())
                        }
                                        dismissDialog()

                    }
                    .addOnFailureListener{exception ->
                                        dismissDialog()
                        Log.w(TAG,"Error getting documents:",exception)
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getAddress() {
        val docRef = db!!.collection(FirestoreTable.Address).whereEqualTo("mainmemberid",id?.id!!.trim())
        docRef.addSnapshotListener { value, error ->
            try {
                if (error != null) {
                    Debug.e("Listen failed.", error.message.toString())
                    return@addSnapshotListener
                }
                if (value!!.isEmpty.not() || value != null) {
                    val item = value.toObjects(AddressData::class.java)
                    addressAdepter.clear()
                    addressAdepter.addAll(item)
                    if(item.size>0) {
                        addressAdepter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    inner class SlideMenuClickListener : TopBarClickListener {
        override fun onTopBarClickListener(view: View?, value: String?) {
            Utils.hideKeyBoard(getContext(), view!!)
            if (value.equals(getLabelText(R.string.back))) {
                try {
                    (mContext as Activity).finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onBackClicked(view: View?) {
            (mContext as Activity).finish()
        }
    }


    inner class ViewClickHandler {

        fun onAddMembar(view: View) {
            try {
                var intent = Intent(mContext, AddMainDetailActivity::class.java)
                intent.putExtra("id",id)
                mContext.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}