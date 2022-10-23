package cn.ddh.simulatedclick

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import cn.ddh.simulatedclick.bean.AgentInfo
import cn.ddh.simulatedclick.event.BackEvent
import cn.ddh.simulatedclick.event.ClickEvent
import cn.ddh.simulatedclick.event.EventBase
import cn.ddh.simulatedclick.event.SlideEvent
import cn.ddh.simulatedclick.net.RetrofitRequest
import cn.ddh.simulatedclick.zxing.utils.ToastUtil

class ViewModel(private val listener: OnWorkDoneListener) {


    private val keyCode: String = "data_event"

    interface OnWorkDoneListener {
        fun done()
        fun click(point: Point, onSimulationResultListener: ViewModel.OnSimulationResultListener?)
        fun move(start: Point, end: Point, onSimulationResultListener: ViewModel.OnSimulationResultListener?)
        fun back(onSimulationResultListener: ViewModel.OnSimulationResultListener?)
        fun agentInfo(times: Int)
    }

    interface OnSimulationResultListener {
        fun result(boolean: Boolean)
    }

    fun toWork(eventBase: EventBase) {


        Log.d("test_dev_hjd", "toWork: 执行任务")
        val obtainMessage = Message()
        val bundle = Bundle()
        bundle.putSerializable(keyCode, eventBase)
        obtainMessage.data = bundle
        obtainMessage.what = 100
        handler.sendMessageDelayed(obtainMessage, eventBase.getDelay().toLong())
    }

    fun cancelAllView() {
        handler.removeMessages(100)
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) = toDealMsg(msg)
    }

    private fun toDealMsg(msg: Message) {
        when (val eventBase = msg.data.getSerializable(keyCode) as EventBase) {
            is ClickEvent -> {
                listener.click(eventBase.getPoint(), object : OnSimulationResultListener {
                    override fun result(boolean: Boolean) {
                        LOGPRINT("点击", boolean)
                    }
                })
            }
            is SlideEvent -> {
                val test: SlideEvent = eventBase
                val point1 = test.getStartPoint()
                val point2 = test.getEndPoint()
                listener.move(point1, point2, object : OnSimulationResultListener {
                    override fun result(boolean: Boolean) {
                        LOGPRINT("滑动", boolean)

                    }
                })
            }
            is BackEvent -> {
                listener.back(object : OnSimulationResultListener {
                    override fun result(boolean: Boolean) {
                        LOGPRINT("返回", boolean)

                    }
                })
            }
        }
        listener.done()
    }

    private fun LOGPRINT(s: String, result: Boolean) {
        Log.d("test_dev", s + "结果:$result")
        ToastUtil.show(s + "结果:$result")
    }


    fun requestAgent() {
        val token = App.INSTANCE.currentUserinfo?.token
        if (TextUtils.isEmpty(token)) {
            ToastUtil.show("请先登录")
            return
        }
        RetrofitRequest.getInstance().agentInfo(token!!).enqueue(object : BaseCallback<AgentInfo> {
            override fun onNext(data: AgentInfo?) {
                listener.agentInfo(data?.times ?: -1)
            }
        })


    }
}