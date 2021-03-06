package com.temp.ui.home.view

import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.temp.R
import com.temp.base.view.BaseActivity
import com.temp.databinding.ActivityHomeBinding
import com.temp.ui.home.viewmodel.HomeViewModel



class HomeActivity : BaseActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProvider(activity).get(HomeViewModel::class.java)
        viewModel.setBinder(binding)
    }
}