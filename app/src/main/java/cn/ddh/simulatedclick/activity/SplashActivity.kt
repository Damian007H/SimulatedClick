package cn.ddh.simulatedclick.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.ddh.simulatedclick.activity.login.LoginActivity
import com.tencent.bugly.crashreport.CrashReport

class SplashActivity : BaseActivity() {

    private val REQUEST_CODE_WRITE_STORAGE = 3001
    private val REQUEST_CODE = 2001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_STORAGE)
        } else {
            startFloat()
        }


    }

    private fun startFloat() {

        val float = getFloat()
        Log.d("test_dev", "startFloat: $float")

        if (float) toNext() else startSetting()
    }


    private fun getFloat(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(this)
    } else {
        Toast.makeText(this, "版本过低，不支持此功能", Toast.LENGTH_SHORT).show()
        false
    }

    private fun startSetting() {
        try {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION), REQUEST_CODE)
        } catch (e: Exception) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE) {
            if (getFloat()) {
                toNext()
            } else {
                Toast.makeText(this, "悬浮窗权限开启失败", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun toNext() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 用户成功授予权限
                startFloat()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    showToast("请到设置中同意存储权限")
                } else {
                    showToast("请务必同意存储权限")

                }
            }
        }
    }
}