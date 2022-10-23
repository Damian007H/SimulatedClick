package cn.ddh.simulatedclick

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import cn.ddh.simulatedclick.event.ClickEvent
import cn.ddh.simulatedclick.event.EventBase
import cn.ddh.simulatedclick.zxing.utils.ToastUtil


class FloatingService : AccessibilityService(), FloatTouchListener.OnFloatTouchListener, ViewModel.OnWorkDoneListener { //    private var isRunning = false
    private val MESSAGE_WHAT_STOP = 1
    private val MESSAGE_WHAT_RUN = 2


    //    private enum class MessageWhat {
    //        STOP, RUN
    //    }


    object INSTANCE {
        var lastBitmap: Bitmap? = null
    }

    //    private var mMediaProjection: MediaProjection? = null
    //    private var listener: MainActivityListener? = null


    //        private var autoTask = false // 默认任务都在停止状态


    private val viewModel: ViewModel = ViewModel(this)
    override fun onInterrupt() {

    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_WHAT_STOP -> {
                    ivFun.setImageResource(R.mipmap.icon_play)

                }
                MESSAGE_WHAT_RUN -> {
                    ivFun.setImageResource(R.mipmap.icon_puse)
                }


            }
        }
    }



    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("test_dev", "onAccessibilityEvent: ")
    }

    private var windowManager: WindowManager? = null

    private var windowLayoutParams: WindowManager.LayoutParams? = null

    private lateinit var parentView: View

    private val eventList = mutableListOf<EventBase>()

    private var currentTaskPos = -1

    private lateinit var ivFun: ImageView
    private lateinit var btOpen: Button
    private lateinit var btTask: Button


    override fun onCreate() {
        super.onCreate() //服务一旦启动，说明  录屏已经准备就绪


        Log.d("aaa_ooo", "onCreate: ")

//        eventList.add(ClickEvent(Point(108, 1201)))
//        eventList.add(ClickEvent(Point(565, 1377)))
//        eventList.add(ClickEvent(Point(784, 1508)))

        eventList.add(ClickEvent(1000,Point(1191, 946)))// task 按钮
//        eventList.add(ClickEvent(Point(1508, 296)))// 弹窗关闭按钮
        eventList.add(ClickEvent(1000,Point(1377, 515)))//go arena challenge
        eventList.add(ClickEvent(1000,Point(350, 947)))//选择第一个角色
        eventList.add(ClickEvent(10000,Point(1082, 920)))// 底部 confirm


        //                eventList.add(ClickEvent(Point(1058, 949)))
        //                eventList.add(ClickEvent(Point(1831, 997)))
        //
        //
        //                eventList.add(ClickEvent(Point(573, 963)))
        //                eventList.add(ClickEvent(Point(1221, 960)))
        //                eventList.add(ClickEvent(Point(2017, 987)))
        //        looperThread = Thread {
        //
        //            while (true) {
        //                Thread.sleep(1000)
        //                if (autoTask) {
        //                    getTopActivity(this) //                    startAllTask()
        //                }
        //            }
        //        }
        //        looperThread?.start()
        //        changeStatusUI()
        initFloatingWindow()
    }


    private fun startAllTask() {
        stopTask()
        currentTaskPos = 0
        startTask()

    }

    private fun initFloatingWindow() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        parentView = LayoutInflater.from(this).inflate(R.layout.layout_float, null)
        windowLayoutParams = setLayoutParams(0)
        initViews()
        windowManager!!.addView(parentView, windowLayoutParams)
    }

    private fun initViews() {

        parentView.setOnTouchListener(FloatTouchListener(this))

        ivFun = parentView.findViewById(R.id.iv_fun)
        btOpen = parentView.findViewById(R.id.bt_open)
        btTask = parentView.findViewById(R.id.bt_task)

        //        ivFun.setOnTouchListener(FloatTouchListener(this))
        //        ivFun.setOnClickListener {
        //            val intent = Intent("cn.ddh.simulatedclick.click")
        //            sendBroadcast(intent)
        //        }
        btOpen.setOnClickListener {
            val home = ForegroundAppUtil.getForegroundActivityName(this)

            if (TextUtils.isEmpty(home)) {
                ToastUtil.show("缺少查看包名权限")
                return@setOnClickListener
            }
            if ("com.gamegff.aets" == home) {
                ToastUtil.show("游戏已打开，请勿重复开启")
            } else { //通过包名启动
                val packageManager: PackageManager = packageManager
                val intent = packageManager.getLaunchIntentForPackage("com.gamegff.aets")
                if (null != intent) {
                    startActivity(intent)
                } else {
                    ToastUtil.show("游戏未安装")
                }


            }
        }

        btTask.setOnClickListener {
            excutorAranaChallenge()

        }


    }


    /**
     * 执行竞技场模式任务
     */
    private fun excutorAranaChallenge() {

        val lastBitmap = INSTANCE.lastBitmap
        lastBitmap?.let { //            for (height in 0 until it.height) {
            //                for (width in 0 until it.width) {
            //                    val pixel = it.getPixel(width, height)
            //                    Log.d("test_Dev", "pixel$pixel")
            //                }
            //            }
        }
        if (true) {
            startAllTask()
        }


        //        Log.d("test_Dev", "excutorAranaChallenge: " + lastBitmap)


    }

    //    private fun getMediaProjectionManager() { //        mMediaProjection = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    //        listener?.click()
    //    }


    //    private fun stop() {
    //        if (isRunning) {
    //            mMediaProjection?.stop()
    //            isRunning = false
    //        }
    //    }

    private fun changeStatusUI() {
        handler.sendEmptyMessage(111)

    }

    private fun startTask() {
        val obtain = Message.obtain()
        obtain.what = MESSAGE_WHAT_RUN
        handler.sendMessage(obtain) //        changeStatusUI()
        if (currentTaskPos < eventList.size) {
            val eventBase = eventList[currentTaskPos]
            eventBase.setTasking()
            viewModel.toWork(eventBase)
        } else {
            allFinish()
        }

    }

    private fun allFinish() {
        stopTask()
    }


    private fun stopTask() {
        for (item in eventList) {
            item.setTasking(false)
        }
        viewModel.cancelAllView()
        currentTaskPos = -1


        val obtain = Message.obtain()
        obtain.what = MESSAGE_WHAT_STOP
        handler.sendMessage(obtain) //        changeStatusUI()

    }


    /**
     * 根据参数生成 LayoutParams
     *
     * @param flag flag
     * @param WH   View 的宽高
     * @param x    初始view的位置x
     * @param y    初始view的位置x
     * @return 对应生成的LayoutParams
     */
    private fun setLayoutParams(flag: Int): WindowManager.LayoutParams {
        val layout: WindowManager.LayoutParams = getFloatLayoutParam(false, true)
        layout.gravity = Gravity.START or Gravity.TOP

        layout.height = WindowManager.LayoutParams.WRAP_CONTENT
        layout.width = WindowManager.LayoutParams.WRAP_CONTENT
        if (flag != 0) {
            layout.flags = layout.flags or flag
        }
        layout.x = 0
        layout.y = 100
        return layout
    }

    /**
     * 获取 FloatLayoutParam
     *
     * @param fullScreen 是否是全屏
     * @param touchAble  是否可触摸
     * @return 对应的layoutParam
     */
    fun getFloatLayoutParam(fullScreen: Boolean, touchAble: Boolean): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY //刘海屏延伸到刘海里面
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }

        //        layoutParams.packageName = getPackageName();
        layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED

        //Focus会占用屏幕焦点，导致游戏无声
        if (touchAble) {
            layoutParams.flags = layoutParams.flags or (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        } else {
            layoutParams.flags = layoutParams.flags or (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        if (fullScreen) {
            layoutParams.flags = layoutParams.flags or (WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            layoutParams.flags = layoutParams.flags or (WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        layoutParams.format = PixelFormat.TRANSPARENT
        return layoutParams
    }


    override fun move(movedX: Int, movedY: Int) {
        windowLayoutParams?.let {
            it.y = it.y + movedY
        }
        windowManager?.updateViewLayout(parentView, windowLayoutParams)
    }


    override fun done() {
        currentTaskPos++
        startTask()
    }

    override fun click(point: Point, onSimulationResultListener: ViewModel.OnSimulationResultListener?) {
        autoClickView(point, onSimulationResultListener)
    }

    override fun move(start: Point, end: Point, onSimulationResultListener: ViewModel.OnSimulationResultListener?) {
        autoSlideView(start, end, onSimulationResultListener)
    }

    override fun back(onSimulationResultListener: ViewModel.OnSimulationResultListener?) {
        autoBackView(onSimulationResultListener)

    }

    override fun agentInfo(times: Int) {

        if (times > 0) {

        } else {
            ToastUtil.show("剩余次数不足")
        }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun autoClickView(point: Point, onTouchDoneListener: ViewModel.OnSimulationResultListener?) {
        val path = Path()
        path.moveTo(point.x.toFloat(), point.y.toFloat())
        val gestureDescription = GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 5)).build()
        dispatchGesture(gestureDescription, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
//                windowLayoutParams?.let {
//                    it.x = point.x
//                    it.y = point.y
//                }
//                windowManager?.updateViewLayout(parentView, windowLayoutParams)
                super.onCompleted(gestureDescription)
                onTouchDoneListener?.result(true)
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                onTouchDoneListener?.result(false)
            }
        }, null)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun autoSlideView(start: Point, end: Point, onTouchDoneListener: ViewModel.OnSimulationResultListener?) {
        val path = Path()
        path.moveTo(start.x.toFloat(), start.y.toFloat())
        path.lineTo(end.x.toFloat(), end.y.toFloat())
        val gestureDescription = GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 500)).build()
        dispatchGesture(gestureDescription, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                onTouchDoneListener?.result(true)
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                onTouchDoneListener?.result(false)
            }
        }, null)
    }

    private fun autoBackView(onSimulationResultListener: ViewModel.OnSimulationResultListener?) = onSimulationResultListener?.result(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) performGlobalAction(GLOBAL_ACTION_BACK) else false) // //    private fun GetandSaveCurrentImage() { //1.构建Bitmap //        val windowManager: WindowManager = windowManager!! //        val display: Display = windowManager.defaultDisplay //        val w: Int = display.getWidth() //        val h: Int = display.getHeight() //        var Bmp: Bitmap //        val decorview: View =window.decorView //        decorview.isDrawingCacheEnabled = true //        Bmp = decorview.drawingCache // //    }

    fun getTopActivity(context: Context): String? {
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        Log.d("测试", "pkg:" + cn!!.packageName) //包名
        Log.d("测试", "cls:" + cn.className) //包名加类名
        return cn.className
    }

    interface MainActivityListener {
        fun click()
    }


}