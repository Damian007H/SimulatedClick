package cn.ddh.simulatedclick

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat


open class BaseDialog(val activity: Activity, layoutId: Int) {

    private var paddingLeft = 0
    private var paddingRight = 0
    private var paddingTop = 0
    private var paddingBottom = 0
    private var alertDialog: AlertDialog? = null

    fun setLeftPadding(padding: Int) {
        paddingLeft = padding
    }

    fun setRightPadding(padding: Int) {
        paddingRight = padding
    }

    fun setTopPadding(padding: Int) {
        paddingTop = padding
    }

    fun setBottomPadding(padding: Int) {
        paddingBottom = padding
    }

    open fun disMiss() {
        if (alertDialog?.isShowing != false) {
            alertDialog?.dismiss()
        }
    }

    open fun show() {
        contentView?.let {
            setWindowLayout(context = it.context)
        }
        alertDialog?.show()

    }

    var contentView: View? = null

    init {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        alertDialog = builder.create()
        val containerView = LayoutInflater.from(activity).inflate(layoutId, null, false)
        contentView = View.inflate(activity, R.layout.dialog_base, null)
        val llParent = contentView!!.findViewById<LinearLayout>(R.id.ll_parent)

        llParent.addView(containerView)

        val windowManager = activity.windowManager
        val defaultDisplay = windowManager.defaultDisplay

        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(defaultDisplay.width, defaultDisplay.height)

        llParent.layoutParams = layoutParams
        setWindowLayout(activity)
        alertDialog!!.setView(contentView)
    }


    fun setWindowLayout(context: Context) {


        val window = alertDialog!!.window!!
        window.decorView.setPadding(paddingLeft, 0, paddingLeft, 0)
        val layoutParams: WindowManager.LayoutParams = window.attributes
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT


        window.attributes = layoutParams
        window.decorView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
    }


    fun setCancelable(able: Boolean) {
        alertDialog?.setCancelable(able)
    }

    fun isShowing(): Boolean = alertDialog?.isShowing ?: false

}