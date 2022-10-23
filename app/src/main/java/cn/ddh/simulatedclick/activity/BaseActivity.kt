package cn.ddh.simulatedclick.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.ddh.simulatedclick.LoadingDialog
import cn.ddh.simulatedclick.zxing.utils.ActivityManager
import cn.ddh.simulatedclick.zxing.utils.ToastUtil

open class BaseActivity : AppCompatActivity() {
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.getInstance().addActivity(this)
    }

    override fun onDestroy() {
        ActivityManager.getInstance().removeActivity(this)

        dismissLoading()
        super.onDestroy()
    }

    fun showToast(msg: String) {
        ToastUtil.show(msg)
    }

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
        }
        loadingDialog?.let {
            if (!it.isShowing()) {
                it.show()
            }
        }

    }

    fun dismissLoading() {
        loadingDialog?.let {
            it.disMiss()
        }

    }

}