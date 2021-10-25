package cn.ddh.simulatedclick

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Message
import cn.ddh.simulatedclick.event.BackEvent
import cn.ddh.simulatedclick.event.ClickEvent
import cn.ddh.simulatedclick.event.EventBase
import cn.ddh.simulatedclick.event.SlideEvent

class ViewModel(private val listener: OnWorkDoneListener) {


    private val keyCode: String = "data_event"

    interface OnWorkDoneListener {
        fun done()
        fun click(point: Point)
        fun move(start: Point, end: Point)
        fun back()
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
                listener.click(point)
            }
            is SlideEvent -> {
                val test: SlideEvent = eventBase
                val point1 = test.getStartPoint()
                val point2 = test.getEndPoint()
                listener.move(point1, point2)
            }
            is BackEvent -> {
                listener.back()
            }
        }
        listener.done()
    }


}