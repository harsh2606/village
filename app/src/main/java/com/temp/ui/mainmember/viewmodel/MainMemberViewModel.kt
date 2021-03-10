package com.temp.ui.mainmember.viewmodel

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityMainMemberBinding
import com.temp.databinding.ActivityVillageListBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.mainmember.datamodel.MainMemberData
import com.temp.ui.mainmember.utilis.MainMemberAdepter
import com.temp.ui.mainmember.view.MainMemberActivity
import com.temp.ui.villagelist.datamodel.VilllageListData
import com.temp.ui.villagelist.utilis.VillageListAdepter

class MainMemberViewModel (application: Application) : BaseViewModel(application) {

    private lateinit var binder: ActivityMainMemberBinding
    private lateinit var mContext: Context
    lateinit var mainMemberAdepter: MainMemberAdepter
    lateinit var  mainMemberData: MainMemberData
    var id: VilllageListData? = null
    val mainMemberList: MutableList<MainMemberData> = ArrayList()


    fun setBinder(binder: ActivityMainMemberBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topbar.topBarClickListener = SlideMenuClickListener()
        this.binder.topbar.isTextShow = true
        this.binder.topbar.isBackShow = true
        mainMemberData= MainMemberData()
        init()
    }

    private fun init() {

        id = (mContext as Activity).intent.extras?.getSerializable("id") as VilllageListData

        binder.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                // filter your list from your input
                if (mainMemberList.isNotEmpty()) {
                    filter(s.toString())
                }
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        })

        getMainMember()
    }

    fun filter(text: String?) {
        val temp: MutableList<MainMemberData> = ArrayList()
        for (d in mainMemberList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (text?.toLowerCase()?.let { d.Name!!.contains(it) } == true) {
                temp.add(d)
            }
        }
        //update recyclerview
        mainMemberAdepter.addAll(temp)
    }

    private fun getMainMember() {
        try {

//            var query = db.collection(FirestoreTable.CHAT)
//                .whereEqualTo(RequestParamsUtils.SENDER_ID, loggedInUserId)
            Debug.e("villageID",id?.id)
            showDialog("",mContext as Activity)
            var query = db!!.collection(FirestoreTable.MainMember).whereEqualTo("villageid",id?.id)


            query.get().addOnSuccessListener { result ->

                if (result != null && result.isEmpty.not()) {
                    val item = result.toObjects(MainMemberData::class.java)
                   mainMemberAdepter = MainMemberAdepter(mContext)
                    mainMemberList.addAll(item)
                    mainMemberAdepter.addAll(item)
                    binder.rvMainMemberList.adapter =  mainMemberAdepter
                    mainMemberAdepter.setEventListener(object : MainMemberAdepter.EventListener {

                        override fun onItemClick(pos: Int, item: MainMemberData) {
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