package cn.ddh.simulatedclick.event

import android.graphics.Point

class ClickEvent(delay: Int, private val point: Point, cycleNum: Int = 1) :
    EventBase("点击", delay, cycleNum = cycleNum) {
    fun getPoint(): Point = point
}