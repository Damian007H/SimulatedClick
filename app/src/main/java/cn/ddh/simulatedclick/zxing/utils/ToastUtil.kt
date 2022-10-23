package cn.ddh.simulatedclick.zxing.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cn.ddh.simulatedclick.App
import cn.ddh.simulatedclick.R

object ToastUtil {
    private var toast: Toast? = null
    fun show(msg: String) {
        if (toast == null) {
            toast = Toast(App.INSTANCE.context)
            toast!!.duration = Toast.LENGTH_SHORT
            val toastview: View = LayoutInflater.from(App.INSTANCE.context).inflate(R.layout.toast_custom, null)
            toast!!.view = toastview
        }

        val view: View = toast!!.view!!
        val toastTextView = view.findViewById<TextView>(R.id.tv_toast_msg)
        toastTextView.text = msg
        toastTextView!!.textSize = 36f
        Log.d("hjd_dev_test", "msg:$msg")
        toast!!.show()
    }
}