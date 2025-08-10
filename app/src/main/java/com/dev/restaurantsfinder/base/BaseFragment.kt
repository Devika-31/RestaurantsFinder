package com.dev.restaurantsfinder.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.restaurantsfinder.base.BaseApplication.Companion.mContext


open class BaseFragment : Fragment() {

    private var toast: Toast? = null
    fun showToast(message: String, isLong: Boolean = false) {
        toast?.cancel()
        toast = Toast.makeText(
            mContext, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        )
        toast?.show()
    }


}