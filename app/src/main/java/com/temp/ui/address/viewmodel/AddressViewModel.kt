package com.temp.ui.address.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityAdressBinding
import com.temp.databinding.ActivityMainMemberBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.address.datamodel.AddressData
import com.temp.ui.address.utilis.AddressAdepter
import com.temp.ui.mainmember.datamodel.MainMemberData
import com.temp.ui.mainmember.utilis.MainMemberAdepter
import com.temp.ui.villagelist.datamodel.VilllageListData

class AddressViewModel (application: Application) : BaseViewModel(application) {
    private lateinit var binder: ActivityAdressBinding
    private lateinit var mContext: Context
    lateinit var addressAdepter: AddressAdepter
    lateinit var  addressData: AddressData
    var id: MainMemberData? = null
    val mainMemberList: MutableList<MainMemberData> = ArrayList()


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
        id = (mContext as Activity).intent.extras?.getSerializable("id") as MainMemberData



        getAddress()
    }



    private fun getAddress() {
        try {

//            var query = db.collection(FirestoreTable.CHAT)
//                .whereEqualTo(RequestParamsUtils.SENDER_ID, loggedInUserId)
            Debug.e("villageID",id?.id)
            showDialog("",mContext as Activity)
            var query = db!!.collection(FirestoreTable.Address)


            query.get().addOnSuccessListener { result ->

                if (result != null && result.isEmpty.not()) {
                    val item = result.toObjects(AddressData::class.java)
                    addressAdepter = AddressAdepter(mContext)
                    addressAdepter.addAll(item)
                    binder.rvAddressList.adapter =  addressAdepter
                    addressAdepter.setEventListener(object : AddressAdepter.EventListener {

                        override fun onItemClick(pos: Int, item: AddressData) {
                        }
                    })
                    Debug.e("Get All Data Successfully")
                }
                dismissDialog()
            }.addOnFailureListener {
                it.printStackTrace()
                dismissDialog()
            }.addOnCompleteListener {
                dismissDialog()
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

    }

}