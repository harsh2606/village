package com.temp.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.temp.R
import com.temp.apputils.Debug
import com.temp.apputils.ErrorAlertDialog
import com.temp.apputils.Utils
import com.temp.base.view.BaseActivity
import com.temp.databinding.ActivitySplashBinding
import com.temp.exceptions.networks.NoInternetException
import com.temp.ui.MyApplication
import com.temp.ui.home.view.HomeActivity
import com.temp.ui.login.view.LoginActivity
import java.io.PrintWriter
import java.io.StringWriter


class SplashActivity : BaseActivity() {
    internal var TAG = "SplashActivity"
    internal var handler = Handler()
    internal lateinit var binding: ActivitySplashBinding

    internal var startApp: Runnable = object : Runnable {
        override fun run() {
            handler.removeCallbacks(this)

            if (Utils.isUserLoggedIn(activity)!!) {
                val i = Intent(activity, HomeActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }  else {
                val i = Intent(activity, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.common_bg)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        (application as MyApplication).initFirebase()

        if (Debug.DEBUG_EXCEPTION) {
            Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->

                try {
                    val sw = StringWriter()
                    paramThrowable.printStackTrace(PrintWriter(sw))
                    Utils.sendCrashReport(sw.toString(), activity)
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
            }
        }
        Utils.getHashKey()


        try {
            if (Utils.isInternetConnected(activity)) {


                checkPermission(this, permissionsListener)

            } else {
                handler.postDelayed(dialogRunnable, 100)
            }


        } catch (e: NoInternetException) {
            e.printStackTrace()
        }
        //  getFirebaseToken()
    }

    private val permissionsListener = object : PermissionListener {
        override fun onGranted() {
            startApplication(1000)

        }

        override fun onDenied() {
            showPermissionAlert()
        }
    }


    public fun startApplication(sleepTime: Long) {
        handler.postDelayed(startApp, sleepTime)
    }

    public override fun onDestroy() {
        try {
            handler.removeCallbacks(startApp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }

    private fun showInternetDialog() {
        ErrorAlertDialog(activity).setTitle(getString(R.string.connection_title))
            .setMessage(getString(R.string.connection_not_available))
            .setNegativeButton(activity.getString(R.string.cancel))
            .setPositiveButton(activity.getString(R.string.settings))
            .setOnButtonClickListener(object : ErrorAlertDialog.DialogButtonClick {
                override fun onPositiveClick() {
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    startActivity(intent)
                }

                override fun onNegativeClick() {
                    finish()
                }
            }).show()


    }

    internal var dialogRunnable: Runnable = object : Runnable {
        override fun run() {
            handler.removeCallbacks(this)
            showInternetDialog()
        }
    }
}
