package com.temp.ui.addmaindetail.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.temp.R
import com.temp.base.view.BaseActivity
import com.temp.databinding.ActivityAddMainDetailBinding
import com.temp.databinding.ActivityAddMembarBinding
import com.temp.ui.addmaindetail.viewmodel.AddMainDetailViewModel
import com.temp.ui.addmember.viewmodel.AddMemberViewModel

class AddMainDetailActivity : BaseActivity() {
    lateinit var binding: ActivityAddMainDetailBinding
    lateinit var viewModel: AddMainDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_main_detail)
        viewModel = ViewModelProvider(activity).get(AddMainDetailViewModel::class.java)
        viewModel.setBinder(binding)
    }
}