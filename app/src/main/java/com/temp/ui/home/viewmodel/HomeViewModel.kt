package com.temp.ui.home.viewmodel


import android.app.*
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.google.firebase.storage.UploadTask
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageClickListener
import com.synnapps.carouselview.ImageListener
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.FirestoreTable
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityHomeBinding
import com.temp.interfaces.TopBarClickListener
import com.temp.ui.MyApplication
import com.temp.ui.home.datamodel.Gallery

class HomeViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var binder: ActivityHomeBinding
    private lateinit var mContext: Context

var sampleImages = intArrayOf(
    R.drawable.image,
    R.drawable.fruit,
    R.drawable.image,
    R.drawable.fruit
)
    var adsList = mutableListOf<Gallery>()

    fun setBinder(binder: ActivityHomeBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        this.binder.topbar.topBarClickListener = SlideMenuClickListener()
        this.binder.topbar.isTextShow = true
        this.binder.topbar.isNavShow = true
        this.binder.topbar.isBackShow = false
        this.binder.topbar.tvTitleText.text = (mContext as Activity).getString(R.string.home)

        init()

    }

    private fun init() {

        initDrawer(mContext)
        getImgList()
    }

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView?) {
//                // You can use Glide or Picasso here
            val urlLogo = adsList[position].image
            Utils.loadImage(imageView!!, urlLogo!!,mContext, R.drawable.placeholder)
        }
    }




    private fun getImgList() {
        try {

            var query = db!!.collection(FirestoreTable.Gallery)

            query.get().addOnSuccessListener { result ->
                if (result != null && result.isEmpty.not()) {
                    val item = result.toObjects(Gallery::class.java)
                    adsList.addAll(item)
                    binder.carouselView.setImageListener(imageListener)
                    binder.carouselView.pageCount = adsList.size
                    Debug.e("Image Successfully")
                }
                dismissDialog()
            }.addOnFailureListener {
                it.printStackTrace()
                dismissDialog()
            }.addOnCompleteListener {
                dismissDialog()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    inner class SlideMenuClickListener : TopBarClickListener {
        override fun onTopBarClickListener(view: View?, value: String?) {
            Utils.hideKeyBoard(getContext(), view!!)
            if (value.equals(getLabelText(R.string.menu))) {
                try {
                    onTopMenuClick()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onBackClicked(view: View?) {
            (mContext as Activity).finish()
        }
    }


    inner class ViewClickHandler {


        fun onSearch(view: View) {
            try {

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}



