package cn.ddh.simulatedclick

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    private lateinit var tvShow: TextView



    private val REQUEST_CODE = 2001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvShow = findViewById(R.id.tv_show)
        startFloat()
    }


    private fun startSetting() {
        try {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION), REQUEST_CODE)
        } catch (e: Exception) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
            e.printStackTrace()
        }

    }

    private fun startService() {

        checkAccessibility()


    }

    private fun checkAccessibility() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)

        startService(Intent(this, FloatingService::class.java))

    }

    private fun startFloat() = if (getFloat()) startService() else startSetting()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE) {
            if (getFloat()) {
                startService()
            } else {
                Toast.makeText(this, "悬浮窗权限开启失败", Toast.LENGTH_SHORT).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun getFloat(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(this)
    } else {
        Toast.makeText(this, "版本过低，不支持此功能", Toast.LENGTH_SHORT).show()
        false
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val movedX = event?.rawX?.toInt() ?: 0
        val movedY = event?.rawY?.toInt() ?: 0
        tvShow.text = "$movedX,$movedY"
        return super.onTouchEvent(event)
    }


}