package com.temp.ui.address.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.firebase.messaging.Constants.TAG
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityAdressBinding
import com.temp.databinding.ActivityMainMemberBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.addmaindetail.view.AddMainDetailActivity
import com.temp.ui.addmember.view.AddMembarActivity
import com.temp.ui.address.datamodel.AddressData
import com.temp.ui.address.utilis.AddressAdepter
import com.temp.ui.address.view.AdressActivity
import com.temp.ui.mainmember.datamodel.MainMemberData
import com.temp.ui.mainmember.utilis.MainMemberAdepter
import com.temp.ui.villagelist.datamodel.VilllageListData

class AddressViewModel (application: Application) : BaseViewModel(application) {
    private lateinit var binder: ActivityAdressBinding
    private lateinit var mContext: Context
    lateinit var addressAdepter: AddressAdepter
    lateinit var  addressData: AddressData
    var id: MainMemberData? = null


    fun setBinder(binder:ActivityAdressBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topbar.topBarClickListener = SlideMenuClickListener()
        this.binder.topbar.isTextShow = true
        this.binder.topbar.isBackShow = true
       addressData=AddressData()
        init()
    }

    private fun init() {
        id = (mContext as Activity).intent.extras?.getSerializable("id") as MainMemberData?
//         binder.tvAddress.setText(id!!.address)

        addressAdepter = AddressAdepter(mContext)
        binder.rvAddressList.adapter =addressAdepter
        addressAdepter.setEventListener(object : AddressAdepter.EventListener {
            override fun onItemClick(pos: Int, item: AddressData) {


            }
        })

        getAddress()
    }



    private fun getAddress() {
        try {

            Debug.e("villageID",id?.id)
            showDialog("",mContext as Activity)
//            var query = db!!.collection("address").whereEqualTo("mainmemberid",id?.id!!.trim())
//
//
//            query.get().addOnSuccessListener { result ->
//
//                if (result != null && result.isEmpty.not()) {
//                    val item = result.toObjects(AddressData::class.java)
////                    addressAdepter.addAll(item)
//                    Debug.e("Get All Data Successfully"+item.toString())
//                }
//                dismissDialog()
//            }.addOnFailureListener {
//                it.printStackTrace()
//                dismissDialog()
//            }.addOnCompleteListener {
////                if (it.result != null && it.result.isEmpty.not()) {
////                    val item = it.result.toObjects(AddressData::class.java)
////                    addressAdepter = AddressAdepter(mContext)
////                    addressAdepter.addAll(item)
////                    binder.rvAddressList.adapter =  addressAdepter
////                    addressAdepter.setEventListener(object : AddressAdepter.EventListener {
////
////                        override fun onItemClick(pos: Int, item: AddressData) {
////                        }
////                    })
////                    Debug.e("Get All Data Successfully")
////                }
//                dismissDialog()
//            }


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