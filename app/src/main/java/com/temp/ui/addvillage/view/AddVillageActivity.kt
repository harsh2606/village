package com.temp.ui.addvillage.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.temp.R
import com.temp.base.view.BaseActivity
import com.temp.databinding.ActivityAddVillageBinding
import com.temp.ui.addvillage.viewmodel.AddVillageViewmodel

class AddVillageActivity : BaseActivity() {
    lateinit var binding: ActivityAddVillageBinding
    lateinit var viewModel: AddVillageViewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_village)
        viewModel = ViewModelProvider(activity).get(AddVillageViewmodel::class.java)
        viewModel.setBinder(binding)
    }
}