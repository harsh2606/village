package com.temp.ui.addmember.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import android.widget.AdapterView
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityAddMembarBinding
import com.temp.databinding.ActivityAddVillageBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.addvillage.datamodel.AddVillage
import com.temp.ui.mainmember.datamodel.MainMemberData
import java.util.*

class AddMemberViewModel (application: Application) : BaseViewModel(application),
    AdapterView.OnItemSelectedListener {

    private lateinit var binder: ActivityAddMembarBinding
    private lateinit var mContext: Context
  lateinit var  mainMemberData: MainMemberData
    var id: AddVillage? = null
    private val TAG = "AddMemberModel"



    fun setBinder(binder: ActivityAddMembarBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topbar.topBarClickListener = SlideMenuClickListener()
        this.binder.topbar.isTextShow = true
        this.binder.topbar.isBackShow = true
        this.binder.topbar.tvTitleText.text = (mContext as Activity).getString(R.string.add_member)
        mainMemberData= MainMemberData()
        init()
    }

    private fun init() {
//        id = (mContext as Activity).intent.extras?.getSerializable("id") as AddVillage

    }

    fun addMember(member:MainMemberData) {

        showDialog("", mContext as Activity)
        member.id = db!!.collection(FirestoreTable.MainMember).document().id
        db!!.collection(FirestoreTable.MainMember)
            .document(member.id!!)
            .set(member)
            .addOnSuccessListener {
                dismissDialog()
                showToast("Member Added Successfully")
                Debug.e(TAG, "Member Added Successfully")
                (mContext as Activity).finish()
            }
            .addOnFailureListener { exception ->
                dismissDialog()
                exception.printStackTrace()
            }
    }




    inner class ViewClickHandler {

        fun onAddMember(view: View) {
            try {
                if (isValidate()) {
                    mainMemberData?.name = binder.edtName.text.toString()
                    mainMemberData?.number = binder.edtNumber.text.toString()
                    mainMemberData?.address = binder.edtAddress.text.toString()
//                    addEvents?.eventsDetail = binder.edtEventDetail.text.toString()
//                    mainMemberData!!.villageid
                    addMember(mainMemberData!!)
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

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private fun isValidate(): Boolean {

        if (binder.edtName.text.toString().isEmpty()) {
            showToast((mContext as Activity).getString(R.string.add_village))
            return false
        }


        return true

    }

}