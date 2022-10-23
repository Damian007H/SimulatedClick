package cn.ddh.simulatedclick.activity.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.ddh.simulatedclick.FloatingService
import cn.ddh.simulatedclick.R
import cn.ddh.simulatedclick.activity.BaseActivity
import cn.ddh.simulatedclick.databinding.ActivityHomeBinding
import cn.ddh.simulatedclick.zxing.activity.CaptureActivity
import cn.ddh.simulatedclick.zxing.utils.ActivityManager
import cn.ddh.simulatedclick.zxing.utils.ToastUtil
import kotlin.collections.ArrayList

class HomeActivity : BaseActivity(), View.OnClickListener {
    private lateinit var homeVM: HomeVM
    private lateinit var activityHomeBinding: ActivityHomeBinding
    private val requestCodeScan = 23
    private val requestCodeCamera = 35
    private val REQUEST_CODE_SCREEN_CAPTURE = 4302
    var isRunning = false

    private val SCREENCAP_NAME = "screencap"
    var IMAGES_PRODUCED = 0

    var mProjectionManager: MediaProjectionManager? = null
    private var mMediaProjection: MediaProjection? = null

    private var mDensity = 0
    private var mWidth = 0
    private var mHeight = 0
    private var mImageReader: ImageReader? = null //    private var mapList:ArrayList<Int>  =  arrayListOf() //    private var index:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        homeVM = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(HomeVM::class.java)

        activityHomeBinding.vm = homeVM
        activityHomeBinding.lifecycleOwner = this

        homeVM.setBinding(activityHomeBinding, this)

        activityHomeBinding.btSubmit.setOnClickListener(this)
        activityHomeBinding.ivScan.setOnClickListener(this)
        activityHomeBinding.btExit.setOnClickListener(this)

        activityHomeBinding.btStart.setOnClickListener(this)
        mProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager


        startScreenCapture()

    }


    private fun startScreenCapture() {
        startActivityForResult(mProjectionManager?.createScreenCaptureIntent(), REQUEST_CODE_SCREEN_CAPTURE)

    }

    //    private fun registerClickReceiver() {
    //        val filter = IntentFilter("cn.ddh.simulatedclick.click") //        clickBroadcastReceiver = ClickBroadcast(this,)
    ////        clickBroadcastReceiver = ClickBroadcast(this, REQUEST_CODE_SCREEN_CAPTURE, mProjectionManager)
    ////        registerReceiver(clickBroadcastReceiver, filter)
    //    }

    override fun onClick(v: View?) {

        v?.let {
            when (it.id) {
                R.id.bt_exit -> {
                    ActivityManager.getInstance().exit()
                }
                R.id.bt_start -> {
                    if (isRunning) {
                        showToast("请勿重复打开")
                    } else {
                        startScreenCapture()

                    }
                }
                R.id.bt_submit -> {
                    val content = activityHomeBinding.etContent.text.toString().trim()
                    if (content == "") {
                        showToast("请输入校验码")
                        return
                    }
                    homeVM.submit(content)
                }
                R.id.iv_scan -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), requestCodeCamera)
                        } else {
                            startCapture()
                        }
                    } else {
                        startCapture()
                    }
                }
                else -> {
                }
            }
        }
    }


    private fun startCapture() {
        startActivityForResult(Intent(this, CaptureActivity::class.java), requestCodeScan)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeScan && data != null) {
            val stringExtra: String? = data.getStringExtra("result")
            stringExtra?.let {
                activityHomeBinding.etContent.setText(it)
            }
        } else if (requestCode == REQUEST_CODE_SCREEN_CAPTURE) {

            if (resultCode == RESULT_OK) {
                mMediaProjection = mProjectionManager!!.getMediaProjection(resultCode, data!!)
                val metrics = resources.displayMetrics
                mDensity = metrics.densityDpi

                val wm = baseContext.getSystemService(WINDOW_SERVICE) as WindowManager
                val display = wm.defaultDisplay
                val size = Point()
                display.getSize(size)
                mWidth = size.x
                mHeight = size.y

                mImageReader = ImageReader.newInstance(mWidth, mHeight, 0x01, 2)
                mMediaProjection?.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader?.surface, null, null)
                mImageReader?.setOnImageAvailableListener(ImageAvailableListener(), null)
                isRunning = true
                startService()
            } else {
                ToastUtil.show("请务必同意录制")
            }


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCamera) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 用户成功授予权限
                startCapture()
            } else {
                showToast("请同意相机权限")
            }

        }
    }

    fun agentInfo(times: Int) { // 准备开始执行点击

        //        dfasfa
        //        val intent = Intent("cn.ddh.simulatedclick.click")
        //        sendBroadcast(intent)
        showToast("剩余次数$times")
    }

    private fun startService() { //        val floatService = Intent(this, FloatingService::class.java)
        //        floatService.putExtra("saa", "sdfdasf")
        val accessibilityIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(accessibilityIntent)
        val floatingIntent = Intent(this, FloatingService::class.java)
        startService(floatingIntent)

    }

    inner class ImageAvailableListener : ImageReader.OnImageAvailableListener {
        override fun onImageAvailable(reader: ImageReader) {
            try {
                val image = reader.acquireLatestImage()
                image?.let {
                    val name = System.currentTimeMillis().toString()
                    IMAGES_PRODUCED++
                    if (IMAGES_PRODUCED % 10 == 0) {
                        saveImage(image, name)
                    }
                    it.close()

                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveImage(image: Image, name: String) {
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * mWidth

        val bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        val pixel = bitmap.getPixel(0, 0)

        FloatingService.INSTANCE.lastBitmap = bitmap

        //
        //        val bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.test_01)
        //        val bitmap2 = BitmapFactory.decodeResource(resources, R.mipmap.test_02)
        Log.d("hjd_dev_test", "截图$pixel")
    }

}