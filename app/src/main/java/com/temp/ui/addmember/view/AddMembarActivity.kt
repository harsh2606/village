package com.temp.ui.addmember.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.temp.R
import com.temp.base.view.BaseActivity
import com.temp.databinding.ActivityAddMembarBinding
import com.temp.databinding.ActivityAddVillageBinding
import com.temp.ui.addmember.viewmodel.AddMemberViewModel
import com.temp.ui.addvillage.viewmodel.AddVillageViewmodel

class AddMembarActivity : BaseActivity() {
    lateinit var binding: ActivityAddMembarBinding
    lateinit var viewModel: AddMemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_membar)
        viewModel = ViewModelProvider(activity).get(AddMemberViewModel::class.java)
        viewModel.setBinder(binding)
    }
}