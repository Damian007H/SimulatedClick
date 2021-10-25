package cn.ddh.simulatedclick.event

import java.io.Serializable

open class EventBase(
    private var name: String,
    private var delay: Int,
    private var isTask: Boolean = false,
    private var cycleNum: Int = 1
) : Serializable {
    fun getDelay(): Int = delay
    fun getName(): String = name
    fun getTasking() = isTask
    fun getCycleNum() = cycleNum
    fun setTasking() {
        this.setTasking(true)
    }

    fun setTasking(boolean: Boolean) {
        this.isTask = boolean
    }
}
