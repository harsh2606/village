package com.temp.ui.addvillage.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.TimePicker
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityAddVillageBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.addvillage.datamodel.AddVillage
import java.text.SimpleDateFormat
import java.util.*

class AddVillageViewmodel (application: Application) : BaseViewModel(application),
    AdapterView.OnItemSelectedListener {

    private lateinit var binder: ActivityAddVillageBinding
    private lateinit var mContext: Context
    var addVillage : AddVillage ?= null
    private val TAG = "AddEventsModel"
    var cal = Calendar.getInstance()


    fun setBinder(binder: ActivityAddVillageBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topbar.topBarClickListener = SlideMenuClickListener()
        this.binder.topbar.isTextShow = true
        this.binder.topbar.isBackShow = true
        this.binder.topbar.tvTitleText.text = (mContext as Activity).getString(R.string.add_village)
        addVillage = AddVillage()
        init()
    }

    private fun init() {


    }

    fun addVillage(village: AddVillage) {

        showDialog("", mContext as Activity)
        village.id = db!!.collection(FirestoreTable.VillageList).document().id
        db!!.collection(FirestoreTable.VillageList)
            .document(village.id!!)
            .set(village)
            .addOnSuccessListener {
                dismissDialog()
                showToast("Village Added Successfully")
                Debug.e(TAG, "Village Added Successfully")
                (mContext as Activity).finish()
            }
            .addOnFailureListener { exception ->
                dismissDialog()
                exception.printStackTrace()
            }
    }



    inner class ViewClickHandler {

        fun onAddEvents(view: View) {
            try {
                if (isValidate()) {
                    addVillage?.village = binder.edtVillage.text.toString()
//                    addEvents?.eventsDetail = binder.edtEventDetail.text.toString()
//                    addEvents!!.userid = Utils.getUserData(mContext)!!.id
                    addVillage(addVillage!!)
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

        if (binder.edtVillage.text.toString().isEmpty()) {
            showToast((mContext as Activity).getString(R.string.add_village))
            return false
        }


        return true

    }

}