package com.temp.ui.address.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.temp.R
import com.temp.base.view.BaseActivity
import com.temp.databinding.ActivityAdressBinding
import com.temp.databinding.ActivityMainMemberBinding
import com.temp.ui.address.viewmodel.AddressViewModel
import com.temp.ui.mainmember.viewmodel.MainMemberViewModel

class AdressActivity : BaseActivity() {
    lateinit var binding: ActivityAdressBinding
    lateinit var viewModel: AddressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adress)
        viewModel = ViewModelProvider(activity).get(AddressViewModel::class.java)
        viewModel.setBinder(binding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}