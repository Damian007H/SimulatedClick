package cn.ddh.simulatedclick

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import cn.ddh.simulatedclick.event.BackEvent
import cn.ddh.simulatedclick.event.ClickEvent
import cn.ddh.simulatedclick.event.EventBase
import cn.ddh.simulatedclick.event.SlideEvent


class AddViewDialogBuilder(
    private val context: Context,
    private val onAddViewListener: OnAddViewListener
) {

    interface OnAddViewListener {
        fun toast(msg: String)
        fun addTask(eventBase: EventBase)
    }

    private lateinit var rootViewGroup: Spinner
    private lateinit var llDelay: LinearLayout
    private lateinit var llCycle: LinearLayout
    private lateinit var etDelay: EditText
    private lateinit var etCycle: EditText
    private lateinit var llPoint1: LinearLayout
    private lateinit var etPoint1x: EditText
    private lateinit var etPoint1y: EditText
    private lateinit var llPoint2: LinearLayout
    private lateinit var etPoint2x: EditText
    private lateinit var etPoint2y: EditText

    private lateinit var tvCancel: TextView
    private lateinit var tvConfirm: TextView


    private var dialogBuilder = AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog)

    private lateinit var dialog: AlertDialog

    private var selectPos = -1

    init {
        dialogBuilder.setMessage("添加任务")
        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null)
        initViews(inflate)
        dialogBuilder.setView(inflate)
        dialogBuilder.setOnDismissListener {
            selectPos = -1
            cleanUI()
        }
    }

    private fun cleanUI() {
        llDelay.visibility = View.GONE
        llPoint1.visibility = View.GONE
        llPoint2.visibility = View.GONE
        llCycle.visibility = View.GONE
        etDelay.setText("1000")
        etCycle.setText("1")
        etPoint1x.setText("")
        etPoint2x.setText("")
        etPoint1y.setText("")
        etPoint2y.setText("")

    }


    private fun initViews(inflate: View) {

        val displayMetrics = context.resources.displayMetrics

        rootViewGroup = inflate.findViewById(R.id.sp_type)

        llDelay = inflate.findViewById(R.id.ll_delay)
        llCycle = inflate.findViewById(R.id.ll_cycle)
        etCycle = inflate.findViewById(R.id.et_cycle)
        etDelay = inflate.findViewById(R.id.et_delay)
        llPoint1 = inflate.findViewById(R.id.ll_point1)
        etPoint1x = inflate.findViewById(R.id.et_point1x)
        etPoint1y = inflate.findViewById(R.id.et_point1y)
        llPoint2 = inflate.findViewById(R.id.ll_point2)
        etPoint2x = inflate.findViewById(R.id.et_point2x)
        etPoint2y = inflate.findViewById(R.id.et_point2y)
        tvConfirm = inflate.findViewById(R.id.tv_confirm)
        tvCancel = inflate.findViewById(R.id.tv_cancel)



        tvConfirm.setOnClickListener {
            clickConfirm()
        }
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }




        rootViewGroup.layoutParams = LinearLayout.LayoutParams(
            displayMetrics.widthPixels - 200,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )


        rootViewGroup.adapter =
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                mutableListOf("点击", "滑动", "返回")
            )
        rootViewGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectPos = position
                changeUI()
            }
        }

    }


    private fun clickConfirm() {
        if (selectPos == -1) {
            onAddViewListener.toast("请确认选择的类型")
        } else {

            val delayStr = etDelay.text.toString().trim()
            if (delayStr == "") {
                onAddViewListener.toast("请输入延迟时间")
                return
            }
            val delayInt = delayStr.toInt()
            if (delayInt <= 0) {
                onAddViewListener.toast("延迟时间不得小于0")
                return
            }


            val cycleNum = etCycle.text.toString().trim()
            if (cycleNum == "") {
                onAddViewListener.toast("请输入循环次数")
                return
            }
            val cycleInt = cycleNum.toInt()
            if (cycleInt <= 0) {
                onAddViewListener.toast("循环次数不得小于0")
                return
            }


            if (selectPos == 0) {

                val etPoint1x = etPoint1x.text.toString().trim()
                val etPoint1y = etPoint1y.text.toString().trim()

                if (etPoint1x == "" || etPoint1y == "") {
                    onAddViewListener.toast("点击坐标未输入")
                    return
                }
                onAddViewListener.addTask(
                    ClickEvent(
                        delayInt,
                        Point(etPoint1x.toInt(), etPoint1y.toInt()), cycleInt
                    )
                )
                dialog.dismiss()


            } else if (selectPos == 1) {

                val etPoint1x = etPoint1x.text.toString().trim()
                val etPoint1y = etPoint1y.text.toString().trim()

                val etPoint2x = etPoint2x.text.toString().trim()
                val etPoint2y = etPoint2y.text.toString().trim()

                if (etPoint1x == "" || etPoint1y == "" || etPoint2x == "" || etPoint2y == "") {
                    onAddViewListener.toast("点击坐标未输入")
                    return
                }
                onAddViewListener.addTask(
                    SlideEvent(
                        delayInt,
                        Point(etPoint1x.toInt(), etPoint1y.toInt()),
                        Point(etPoint2x.toInt(), etPoint2y.toInt()), cycleInt
                    )
                )
                dialog.dismiss()


            } else {
                onAddViewListener.addTask(
                    BackEvent(delayInt, cycleInt)
                )
                dialog.dismiss()

            }


        }


    }

    private fun changeUI() {
        cleanUI()
        if (selectPos != -1) {
            llDelay.visibility = View.VISIBLE
            llCycle.visibility = View.VISIBLE

            when (selectPos) {
                0 -> {//点击
                    llPoint1.visibility = View.VISIBLE
                }
                1 -> {
                    llPoint1.visibility = View.VISIBLE
                    llPoint2.visibility = View.VISIBLE
                }
                2 -> {
                }
            }
        }


    }

    public fun create(): AlertDialog {
        dialog = dialogBuilder.create()


        dialog.window?.setBackgroundDrawableResource(android.R.color.white);

        dialog.window?.setType(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog.setOnShowListener {
            rootViewGroup.setSelection(0)
        }

        return dialog
    }


}