package com.temp.ui.mainmember.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.temp.R
import com.temp.base.view.BaseActivity
import com.temp.databinding.ActivityMainMemberBinding
import com.temp.databinding.ActivityVillageListBinding
import com.temp.ui.mainmember.viewmodel.MainMemberViewModel
import com.temp.ui.villagelist.viewmodel.VillageListViewModel

class MainMemberActivity : BaseActivity() {
    lateinit var binding: ActivityMainMemberBinding
    lateinit var viewModel: MainMemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_member)
        viewModel = ViewModelProvider(activity).get(MainMemberViewModel::class.java)
        viewModel.setBinder(binding)
    }
}