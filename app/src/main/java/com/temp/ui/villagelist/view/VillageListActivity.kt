package com.temp.ui.villagelist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.temp.R
import com.temp.base.view.BaseActivity
import com.temp.databinding.ActivityHomeBinding
import com.temp.databinding.ActivityVillageListBinding
import com.temp.ui.home.viewmodel.HomeViewModel
import com.temp.ui.villagelist.viewmodel.VillageListViewModel

class VillageListActivity: BaseActivity() {
    lateinit var binding: ActivityVillageListBinding
    lateinit var viewModel: VillageListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_village_list)
        viewModel = ViewModelProvider(activity).get(VillageListViewModel::class.java)
        viewModel.setBinder(binding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}