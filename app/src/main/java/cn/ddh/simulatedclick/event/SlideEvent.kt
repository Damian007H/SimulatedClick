package cn.ddh.simulatedclick.event

import android.graphics.Point

class SlideEvent(delay: Int, private val statPoint: Point, private val endPoint: Point, cycleNum: Int = 1) :
    EventBase("滑动", delay,cycleNum = cycleNum) {
    fun getStartPoint(): Point = statPoint
    fun getEndPoint(): Point = endPoint
}