package cn.ddh.simulatedclick

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.ddh.simulatedclick.adapter.EventAdapter
import cn.ddh.simulatedclick.event.EventBase


class FloatingService : AccessibilityService(), FloatTouchListener.OnFloatTouchListener,
        AddViewDialogBuilder.OnAddViewListener, ViewModel.OnWorkDoneListener {

    private var taskIng = false

    private val viewModel: ViewModel = ViewModel(this)
    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {


    }

    private var windowManager: WindowManager? = null

    private var windowLayoutParams: WindowManager.LayoutParams? = null

    private lateinit var addViewDialog: AlertDialog
    private lateinit var parentView: View

    private val eventList = mutableListOf<EventBase>()
    private val eventAdapter =
            EventAdapter(this, eventList)

    private var currentTaskPos = -1

    private lateinit var ivFun: ImageView

    override fun onCreate() {
        super.onCreate()
        initFloatingWindow()
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

        val ivAddIcon = parentView.findViewById<ImageView>(R.id.iv_add)
        val rvContent = parentView.findViewById<RecyclerView>(R.id.rv_content)
        ivFun = parentView.findViewById(R.id.iv_fun)

        addViewDialog = AddViewDialogBuilder(this, this).create()
        ivAddIcon.setOnTouchListener(FloatTouchListener(this))
        rvContent.setOnTouchListener(FloatTouchListener(this))
        ivFun.setOnTouchListener(FloatTouchListener(this))
        ivAddIcon.setOnClickListener { _ ->
            if (!taskIng) {
                addViewDialog.show()
            } else {
                toast("正在执行任务，请先停止")
            }
        }
        ivAddIcon.setOnLongClickListener {
            if (!taskIng) {
                if (eventList.size > 0) {
                    eventList.clear()
                    eventAdapter.notifyDataSetChanged()
                }
            } else {
                toast("正在执行任务，请先停止")
            }
            return@setOnLongClickListener true
        }
        rvContent.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvContent.adapter = eventAdapter
        changeStatusUI()
        ivFun.setOnClickListener {
            if (taskIng) {
                stopTask()
                toast("所有任务已取消执行")
            } else {
                if (eventList.size > 0) {
                    currentTaskPos = 0
                    startTask()
                    taskIng = true
                } else {
                    toast("没有任务可以执行")
                }
            }
            changeStatusUI()
        }


    }

    private fun startTask() {

        if (currentTaskPos < eventList.size) {
            val eventBase = eventList[currentTaskPos]
            eventBase.setTasking()
            eventAdapter.notifyItemChanged(currentTaskPos)
            viewModel.toWork(eventBase)
        } else {
            allFinish()
        }

    }

    private fun allFinish() {
        stopTask()
        toast("所有任务都已经结束")
    }


    private fun stopTask() {

        for (item in eventList) {
            item.setTasking(false)
        }
        eventAdapter.notifyDataSetChanged()
        viewModel.cancelAllView()

        currentTaskPos = -1
        taskIng = false
        changeStatusUI()
    }

    private fun changeStatusUI() =
            ivFun.setImageResource(if (taskIng) R.mipmap.icon_puse else R.mipmap.icon_play)


    /**
     * 根据参数生成 LayoutParams
     *
     * @param flag flag
     * @param WH   View 的宽高
     * @param x    初始view的位置x
     * @param y    初始view的位置x
     * @return 对应生成的LayoutParams
     */
    private fun setLayoutParams(
            flag: Int
    ): WindowManager.LayoutParams {
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
    fun getFloatLayoutParam(
            fullScreen: Boolean,
            touchAble: Boolean
    ): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            //刘海屏延伸到刘海里面
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M
        ) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }

//        layoutParams.packageName = getPackageName();
        layoutParams.flags =
                layoutParams.flags or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED

        //Focus会占用屏幕焦点，导致游戏无声
        if (touchAble) {
            layoutParams.flags =
                    layoutParams.flags or (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        } else {
            layoutParams.flags =
                    layoutParams.flags or (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        if (fullScreen) {
            layoutParams.flags = layoutParams.flags or (WindowManager.LayoutParams.FLAG_FULLSCREEN
                    or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            layoutParams.flags =
                    layoutParams.flags or (WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                            or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
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

    override fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    override fun addTask(eventBase: EventBase) {

        if (eventBase.getCycleNum() >= 0) {
            for (item in 0 until eventBase.getCycleNum()) {
                eventList.add(eventBase)
            }
            eventAdapter.notifyDataSetChanged()
        }
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


    @RequiresApi(Build.VERSION_CODES.N)
    private fun autoClickView(point: Point, onTouchDoneListener: ViewModel.OnSimulationResultListener?) {
        val path = Path()
        path.moveTo(point.x.toFloat(), point.y.toFloat())
        val gestureDescription = GestureDescription.Builder()
                .addStroke(GestureDescription.StrokeDescription(path, 0, 5))
                .build()
        dispatchGesture(
                gestureDescription,
                object : AccessibilityService.GestureResultCallback() {
                    override fun onCompleted(gestureDescription: GestureDescription?) {
                        super.onCompleted(gestureDescription)
                        onTouchDoneListener?.result(true)
                    }

                    override fun onCancelled(gestureDescription: GestureDescription?) {
                        super.onCancelled(gestureDescription)
                        onTouchDoneListener?.result(false)
                    }
                }, null
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun autoSlideView(start: Point, end: Point, onTouchDoneListener: ViewModel.OnSimulationResultListener?) {
        val path = Path()
        path.moveTo(start.x.toFloat(), start.y.toFloat())
        path.lineTo(end.x.toFloat(), end.y.toFloat())
        val gestureDescription = GestureDescription.Builder()
                .addStroke(GestureDescription.StrokeDescription(path, 0, 500))
                .build()
        dispatchGesture(
                gestureDescription,
                object : AccessibilityService.GestureResultCallback() {
                    override fun onCompleted(gestureDescription: GestureDescription?) {
                        super.onCompleted(gestureDescription)
                        onTouchDoneListener?.result(true)
                    }

                    override fun onCancelled(gestureDescription: GestureDescription?) {
                        super.onCancelled(gestureDescription)
                        onTouchDoneListener?.result(false)
                    }
                }, null
        )
    }

    private fun autoBackView(onSimulationResultListener: ViewModel.OnSimulationResultListener?) = onSimulationResultListener?.result(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) performGlobalAction(GLOBAL_ACTION_BACK) else false)
}