package com.temp.ui.villagelist.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import com.google.firebase.firestore.Query
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityVillageListBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.villagelist.datamodel.VilllageListData
import com.temp.ui.villagelist.utilis.VillageListAdepter

class VillageListViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var binder: ActivityVillageListBinding
    private lateinit var mContext: Context
    lateinit var villageListAdepter: VillageListAdepter


    fun setBinder(binder: ActivityVillageListBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        binder.topBar.isTextShow = true
        binder.topBar.isBackShow = true
        binder.topBar.topBarClickListener = SlideMenuClickListener()
        init()
    }

    private fun init() {
//        adapter = SurnameAdapter(mContext)
//        binder.rvSurname.adapter = adapter
//        adapter.setEventListener(object : SurnameAdapter.EventListener {
//            override fun onItemClick(pos: Int, item: SurnameData) {
//                var intent = Intent(mContext, SurnameContactsActivity::class.java)
//                intent.putExtra("surname",item)
//                mContext.startActivity(intent)
//            }
//        })
        getVillageList()
    }

    private fun getVillageList() {
        try {

//            var query = db.collection(FirestoreTable.CHAT)
//                .whereEqualTo(RequestParamsUtils.SENDER_ID, loggedInUserId)
            showDialog("",mContext as Activity)
            var query = db!!.collection(FirestoreTable.VillageList)


            query.get().addOnSuccessListener { result ->
                if (result != null && result.isEmpty.not()) {
                    val item = result.toObjects(VilllageListData::class.java)
                    villageListAdepter.addAll(item)
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
            if (value.equals(getLabelText(R.string.menu))) {
                try {

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onBackClicked(view: View?) {
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