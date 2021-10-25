package cn.ddh.simulatedclick

import android.view.MotionEvent
import android.view.View

open class FloatTouchListener(private val onFloatTouchListener: OnFloatTouchListener) :
    View.OnTouchListener {

    interface OnFloatTouchListener {
        fun move(movedX: Int, movedY: Int)
    }

    private var x = 0
    private var y = 0
    override fun onTouch(v: View, event: MotionEvent?): Boolean {

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                x = event.rawX.toInt()
                y = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val nowX = event.rawX.toInt()
                val nowY = event.rawY.toInt()
                val movedX: Int = nowX - x
                val movedY: Int = nowY - y
                x = nowX
                y = nowY
                onFloatTouchListener.move(movedX, movedY)
            }

        }
        return false

    }
}