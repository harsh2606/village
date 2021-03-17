package com.temp.ui.mainmember.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.firebase.messaging.Constants
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityMainMemberBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.addmember.view.AddMembarActivity
import com.temp.ui.address.view.AdressActivity
import com.temp.ui.addvillage.datamodel.AddVillage
import com.temp.ui.mainmember.datamodel.AddMemberData
import com.temp.ui.mainmember.utilis.MainMemberAdepter

class MainMemberViewModel (application: Application) : BaseViewModel(application) {

    private lateinit var binder: ActivityMainMemberBinding
    private lateinit var mContext: Context
    lateinit var mainMemberAdepter: MainMemberAdepter
    var id: AddVillage? = null

    fun setBinder(binder: ActivityMainMemberBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topBar.topBarClickListener = SlideMenuClickListener()
        this.binder.topBar.isTextShow = true
        this.binder.topBar.isBackShow = true
        this.binder.topBar.tvTitleText.text = (mContext as Activity).getString(R.string.mainmember_list)
        init()
    }

    private fun init() {


        id = (mContext as Activity).intent.extras?.getSerializable("id") as AddVillage

        mainMemberAdepter = MainMemberAdepter(mContext)
        binder.rvMainMemberList.adapter = mainMemberAdepter
      mainMemberAdepter.setEventListener(object : MainMemberAdepter.EventListener {
            override fun onItemClick(pos: Int, item: AddMemberData) {
                var intent = Intent(mContext, AdressActivity::class.java)
                intent.putExtra("id",item)
                mContext.startActivity(intent)



            }
        })

    }
    fun onResume() {
        getMember()
    }

    private fun getMember1() {
        try {

            Debug.e("villageID",id?.id)
            showDialog("",mContext as Activity)

            db!!.collection(FirestoreTable.MainMember).whereEqualTo("villageid",id?.id!!.trim()).get()
                    .addOnSuccessListener{ documents ->
                        var a = documents.size()
                        Debug.e("size",a.toString())
                        if (documents != null && documents.isEmpty.not()) {

                            val item = documents.toObjects(AddMemberData::class.java)
                           mainMemberAdepter.addAll(item)

                            binder.rvMainMemberList.adapter = mainMemberAdepter
                            mainMemberAdepter.setEventListener(object : MainMemberAdepter.EventListener {
                                override fun onItemClick(pos: Int, item: AddMemberData) {
                                    var intent = Intent(mContext, AdressActivity::class.java)
                                    intent.putExtra("id",item)
                                    mContext.startActivity(intent)

                                }
                            })
                            Debug.e("Get All Data Successfully"+item.toString())
                        }
                        dismissDialog()

                    }
                    .addOnFailureListener{exception ->
                        dismissDialog()
                        Log.w(Constants.TAG,"Error getting documents:",exception)
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getMember() {
        val docRef = db!!.collection(FirestoreTable.MainMember).whereEqualTo("villageid",id?.id!!.trim())
        docRef.addSnapshotListener { value, error ->
            try {
                if (error != null) {
                    Debug.e("Listen failed.", error.message.toString())
                    return@addSnapshotListener
                }
                if (value!!.isEmpty.not() || value != null) {
                    val item = value.toObjects(AddMemberData::class.java)
                    mainMemberAdepter.clear()
                    mainMemberAdepter.addAll(item)
                    if(item.size>0) {
                        mainMemberAdepter.notifyDataSetChanged()
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
                var intent = Intent(mContext, AddMembarActivity::class.java)
                intent.putExtra("id",id)
                mContext.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}