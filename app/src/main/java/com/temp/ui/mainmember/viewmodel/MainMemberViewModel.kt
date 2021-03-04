package com.temp.ui.mainmember.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
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
import com.temp.ui.villagelist.datamodel.VilllageListData
import com.temp.ui.villagelist.utilis.VillageListAdepter

class MainMemberViewModel (application: Application) : BaseViewModel(application) {

    private lateinit var binder: ActivityMainMemberBinding
    private lateinit var mContext: Context
    lateinit var mainMemberAdepter: MainMemberAdepter
    lateinit var  mainMemberData: MainMemberData



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

        getMainMember()
    }

    private fun getMainMember() {
        try {

//            var query = db.collection(FirestoreTable.CHAT)
//                .whereEqualTo(RequestParamsUtils.SENDER_ID, loggedInUserId)
            showDialog("",mContext as Activity)
            var query = db!!.collection(FirestoreTable.MainMember)


            query.get().addOnSuccessListener { result ->
                if (result != null && result.isEmpty.not()) {
                    val item = result.toObjects(MainMemberData::class.java)
                   mainMemberAdepter = MainMemberAdepter(mContext)
                    mainMemberAdepter.addAll(item)
                    binder.rvMainMemberList.adapter =  mainMemberAdepter
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
        fun onReviewsAndRanks(view: View) {
            try {
//                var intent = Intent(mContext, ReviewsAndRankActivity::class.java)
//                mContext.startActivity(intent)
//                (mContext as Activity).finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun onSeeAll(view: View) {
            try {
//                var intent = Intent(mContext, ReviewsAndRankActivity::class.java)
//                mContext.startActivity(intent)
//                (mContext as Activity).finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}