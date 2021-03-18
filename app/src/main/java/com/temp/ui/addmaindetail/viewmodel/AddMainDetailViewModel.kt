package com.temp.ui.addmaindetail.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityAddMainDetailBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.address.datamodel.AddressData
import com.temp.ui.mainmember.datamodel.AddMemberData

class AddMainDetailViewModel (application: Application) : BaseViewModel(application),
    AdapterView.OnItemSelectedListener {

    private lateinit var binder: ActivityAddMainDetailBinding
    private lateinit var mContext: Context
    lateinit var  addressData: AddressData
    var id: AddMemberData? = null
    private val TAG = "AddMemberModel"



    fun setBinder(binder: ActivityAddMainDetailBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topbar.topBarClickListener = SlideMenuClickListener()
        this.binder.topbar.isTextShow = true
        this.binder.topbar.isBackShow = true
        this.binder.topbar.tvTitleText.text = (mContext as Activity).getString(R.string.add_member)
        addressData= AddressData()
        init()
    }

    private fun init() {
        id = (mContext as Activity).intent.extras?.getSerializable("id") as AddMemberData

        val adapter = ArrayAdapter.createFromResource(
                mContext,
                R.array.items_spinner_add,
                R.layout.style_spinner_layout
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        binder.spinnerAdd.adapter = adapter
        binder.spinnerAdd.onItemSelectedListener = this



    }

    fun addDetail(detail:AddressData) {

        showDialog("", mContext as Activity)
        detail.id = db!!.collection(FirestoreTable.Address).document().id
        db!!.collection(FirestoreTable.Address)
            .document(detail.id!!)
            .set(detail)
            .addOnSuccessListener {
                dismissDialog()
                showToast("Detail Added Successfully")
                Debug.e(TAG, "Deatil Added Successfully")
                (mContext as Activity).finish()
            }
            .addOnFailureListener { exception ->
                dismissDialog()
                exception.printStackTrace()
            }
    }

    inner class ViewClickHandler {

        fun onAddDetail(view: View) {
            try {
                if (isValidate()) {
                    addressData?.name = binder.edtName.text.toString()
                    addressData?.relation = binder.edtRelation.text.toString()
                    addressData?.business = binder.edtBusiness.text.toString()
                    addressData?.dob = binder.edtDob.text.toString()
                    addressData?.study = binder.edtStudy.text.toString()
                    addressData?.number = binder.edtNumber.text.toString()
                    addressData?.bloodgroup = binder.spinnerAdd.selectedItem.toString()
                    addressData!!.mainmemberid = id!!.id
                    addDetail(addressData!!)
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