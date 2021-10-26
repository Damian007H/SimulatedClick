package cn.ddh.simulatedclick

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import cn.ddh.simulatedclick.event.BackEvent
import cn.ddh.simulatedclick.event.ClickEvent
import cn.ddh.simulatedclick.event.EventBase
import cn.ddh.simulatedclick.event.SlideEvent

class ViewModel(private val listener: OnWorkDoneListener) {


    private val keyCode: String = "data_event"

    interface OnWorkDoneListener {
        fun done()
        fun click(point: Point, onSimulationResultListener: ViewModel.OnSimulationResultListener?)
        fun move(start: Point, end: Point, onSimulationResultListener: ViewModel.OnSimulationResultListener?)
        fun back(onSimulationResultListener: ViewModel.OnSimulationResultListener?)
    }

    interface OnSimulationResultListener {
        fun result(boolean: Boolean)
    }

    fun toWork(eventBase: EventBase) {
        val obtainMessage = Handler().obtainMessage()
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
                val test: ClickEvent = eventBase
                val point = test.getPoint()
                listener.click(point, object : OnSimulationResultListener {
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
        Log.d("test_hjd", "模拟$s,结果:$result")
    }
}