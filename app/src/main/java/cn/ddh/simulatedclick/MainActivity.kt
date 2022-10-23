//package cn.ddh.simulatedclick
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.Point
//import android.hardware.display.DisplayManager
//import android.media.Image
//import android.media.ImageReader
//import android.media.ImageReader.OnImageAvailableListener
//import android.media.projection.MediaProjection
//import android.media.projection.MediaProjectionManager
//import android.os.Build
//import android.os.Bundle
//import android.provider.Settings
//import android.util.Log
//import android.view.MotionEvent
//import android.view.WindowManager
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//
//class MainActivity : AppCompatActivity() {
//
////    private var clickBroadcastReceiver: ClickBroadcast? = null;
//
//    var isRunning = false
//    var IMAGES_PRODUCED = 0
//
//    private val SCREENCAP_NAME = "screencap"
//
//    var mProjectionManager: MediaProjectionManager? = null
//    private var mMediaProjection: MediaProjection? = null
//
//
//    private lateinit var tvShow: TextView
//
//    private var mDensity = 0
//    private var mWidth = 0
//    private var mHeight = 0
//    private var mImageReader: ImageReader? = null
//
//    private val REQUEST_CODE = 2001
//    private val REQUEST_CODE_WRITE_STORAGE = 3001
//    private val REQUEST_CODE_SCREEN_CAPTURE = 4001
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        tvShow = findViewById(R.id.tv_show)
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_STORAGE)
//        } else {
//            startFloat()
//        }
//    }
//
//    private fun btnStart() {
//        if (isRunning) {
//            stop()
//        } else {
//            getMediaProjectionManger()
//
//        }
//    }
//
//
//    private fun startSetting() {
//        try {
//            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION), REQUEST_CODE)
//        } catch (e: Exception) {
//            startActivity(Intent(Settings.ACTION_SETTINGS))
//            e.printStackTrace()
//        }
//
//    }
//
//    private fun startService() {
//        Log.d(TAG, "startService: ")
//        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//        startActivity(intent)
//        startService(Intent(this, FloatingService::class.java))
//
////        val filter = IntentFilter("cn.ddh.simulatedclick.click")
////        clickBroadcastReceiver = ClickBroadcast()
////        registerReceiver(clickBroadcastReceiver, filter)
//    }
//
//    private fun startFloat() = if (getFloat()) startService() else startSetting()
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode == REQUEST_CODE) {
//            if (getFloat()) {
//                startService()
//            } else {
//                Toast.makeText(this, "悬浮窗权限开启失败", Toast.LENGTH_SHORT).show()
//            }
//        } else if (requestCode == REQUEST_CODE_SCREEN_CAPTURE) {
//            mMediaProjection = mProjectionManager!!.getMediaProjection(resultCode, data!!)
//            val metrics = resources.displayMetrics
//            mDensity = metrics.densityDpi
//
//            val wm = baseContext.getSystemService(WINDOW_SERVICE) as WindowManager
//            val display = wm.defaultDisplay
//            val size = Point()
//            display.getSize(size)
//            mWidth = size.x
//            mHeight = size.y
//
//            mImageReader = ImageReader.newInstance(mWidth, mHeight, 0x01, 2)
//            mMediaProjection?.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader?.surface, null, null)
//            mImageReader?.setOnImageAvailableListener(ImageAvailableListener(), null)
//            isRunning = true
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//
//    inner class ImageAvailableListener : OnImageAvailableListener {
//        override fun onImageAvailable(reader: ImageReader) {
//            try {
//
//                val image = reader.acquireLatestImage()
//                image?.let {
//                    val name = System.currentTimeMillis().toString()
//                    IMAGES_PRODUCED++
//                    if (IMAGES_PRODUCED % 10 == 0) {
//                        saveImage(image, name)
//                    }
//                    it.close()
//
//                }
//
//                //                reader.acquireLatestImage().use { image ->
//                //                    if (image != null) {
//                //                        val name = System.currentTimeMillis().toString()
//                //                        MainActivity.IMAGES_PRODUCED++
//                //                        if (MainActivity.IMAGES_PRODUCED % 10 == 0) {
//                //                            saveJpeg(image, name)
//                //                        }
//                //                        image.close()
//                //                    }
//                //                }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    private fun saveImage(image: Image, name: String) {
//        val planes = image.planes
//        val buffer = planes[0].buffer
//        val pixelStride = planes[0].pixelStride
//        val rowStride = planes[0].rowStride
//        val rowPadding = rowStride - pixelStride * mWidth
//
//        val bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888)
//        bitmap.copyPixelsFromBuffer(buffer)
//        val pixel = bitmap.getPixel(0, 0)
//
//        Log.d("hjd_dev_test", "截图$pixel")
//    }
//
//    private fun getFloat(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        Settings.canDrawOverlays(this)
//    } else {
//        Toast.makeText(this, "版本过低，不支持此功能", Toast.LENGTH_SHORT).show()
//        false
//    }
//
//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        val movedX = event?.rawX?.toInt() ?: 0
//        val movedY = event?.rawY?.toInt() ?: 0
//        tvShow.text = "$movedX,$movedY"
//        return super.onTouchEvent(event)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startFloat()
//            } else {
//                Toast.makeText(this, "请同意权限", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onDestroy() {
////        if (clickBroadcastReceiver != null) {
////            unregisterReceiver(clickBroadcastReceiver)
////        }
//        super.onDestroy()
//    }
//
//    private fun stop() {
//        if (isRunning) {
//            mMediaProjection?.stop()
//            isRunning = false
//        }
//    }
//
//    private fun getMediaProjectionManger() {
//
//        mProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
//        if (mProjectionManager != null) {
//            startActivityForResult(mProjectionManager?.createScreenCaptureIntent(), REQUEST_CODE_SCREEN_CAPTURE)
//        }
//
//    }
//
//    inner class ClickBroadcast : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            btnStart()
//        }
//
//    }
//
//
//}