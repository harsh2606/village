package com.temp.ui.villagelist.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.AutoText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.firebase.firestore.Query
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityVillageListBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.addvillage.datamodel.AddVillage
import com.temp.ui.addvillage.view.AddVillageActivity
import com.temp.ui.mainmember.view.MainMemberActivity
//import com.temp.ui.villagelist.datamodel.VilllageListData
import com.temp.ui.villagelist.utilis.VillageListAdepter
import java.util.ArrayList

class VillageListViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var binder: ActivityVillageListBinding
    private lateinit var mContext: Context
    lateinit var villageListAdepter: VillageListAdepter
//    lateinit var  villageListData: VilllageListData
     val villageList:MutableList<AddVillage> = ArrayList()

    fun setBinder(binder: ActivityVillageListBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topBar.topBarClickListener = SlideMenuClickListener()
        this.binder.topBar.isTextShow = true
        this.binder.topBar.isBackShow = true
        this.binder.topBar.tvTitleText.text = (mContext as Activity).getString(R.string.village_list)
//        villageListData = VilllageListData()
        init()
    }

    private fun init() {

        villageListAdepter = VillageListAdepter(mContext)
        binder.rvVillageList.adapter = villageListAdepter
        villageListAdepter.setEventListener(object : VillageListAdepter.EventListener {
            override fun onItemClick(pos: Int, item: AddVillage) {
                var intent = Intent(mContext, MainMemberActivity::class.java)
                intent.putExtra("id",item)
                mContext.startActivity(intent)

            }
        })

        binder.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                // filter your list from your input
                if (villageList.isNotEmpty()) {
                    filter(s.toString())
                }
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        })


    }

    fun filter(text: String?) {
        val temp: MutableList<AddVillage> = ArrayList()
        for (d in villageList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (text?.toLowerCase()?.let { d.village!!.contains(it) } == true) {
                temp.add(d)
            }
        }
        //update recyclerview
        villageListAdepter.addAll(temp)
    }


    fun onResume() {
        getVillageList()
    }



    private fun getVillageList1() {
        try {

            var query = db!!.collection(FirestoreTable.VillageList).orderBy("village")

            showDialog("",(mContext as Activity))

            query.get().addOnSuccessListener { result ->
                dismissDialog()
                if (result != null && result.isEmpty.not()) {
                    val item = result.toObjects(AddVillage::class.java)
                    villageListAdepter = VillageListAdepter(mContext)
                    villageListAdepter.addAll(item)
                    villageList.addAll(item)
                    binder.rvVillageList.adapter = villageListAdepter
                    villageListAdepter.setEventListener(object : VillageListAdepter.EventListener {
                        override fun onItemClick(pos: Int, item: AddVillage) {
                            var intent = Intent(mContext, MainMemberActivity::class.java)
                            intent.putExtra("id",item)
                            mContext.startActivity(intent)

                        }
                    })
                }

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

    fun getVillageList() {
        val docRef = db!!.collection(FirestoreTable.VillageList).orderBy("village")
        docRef.addSnapshotListener { value, error ->
            try {
                if (error != null) {
                    Debug.e("Listen failed.", error.message.toString())
                    return@addSnapshotListener
                }
                if (value!!.isEmpty.not() || value != null) {
                    val item = value.toObjects(AddVillage::class.java)
                    villageListAdepter.clear()
                    villageListAdepter.addAll(item)
                    if(item.size>0) {
                        villageListAdepter.notifyDataSetChanged()
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
        fun onAddVillage(view: View) {
            try {
                var intent = Intent(mContext, AddVillageActivity::class.java)
                mContext.startActivity(intent)
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