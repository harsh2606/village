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

//    var sampleImages = intArrayOf(
//            R.drawable.image,
//            R.drawable.fruit,
//            R.drawable.image,
//            R.drawable.fruit
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProvider(activity).get(HomeViewModel::class.java)
        viewModel.setBinder(binding)

//        val carouselView = findViewById(R.id.carouselView) as CarouselView;
//        carouselView.setPageCount(sampleImages.size);
//        carouselView.setImageListener(imageListener);
    }

//    var imageListener: ImageListener = object : ImageListener {
//        override fun setImageForPosition(position: Int, imageView: ImageView) {
//            // You can use Glide or Picasso here
//            imageView.setImageResource(sampleImages[position])
//        }
//    }

}
