package cn.ddh.simulatedclick.event

import android.graphics.Point

class ClickEvent(delay:Int, private val point: Point) :
    EventBase("点击", delay, cycleNum = 1) {
    fun getPoint(): Point = point
}