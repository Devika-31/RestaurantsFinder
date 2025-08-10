package com.dev.restaurantsfinder.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.restaurantsfinder.base.BaseApplication.Companion.mContext
import androidx.core.content.ContextCompat
import android.content.res.Configuration

open class BaseActivity : AppCompatActivity() {
    private var toast: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    fun showToast(message: String, isLong: Boolean = false) {
        toast?.cancel()
        toast = Toast.makeText(
            mContext, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        )
        toast?.show()
    }


}