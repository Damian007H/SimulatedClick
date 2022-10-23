package cn.ddh.simulatedclick

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import cn.ddh.simulatedclick.activity.BaseActivity

open class LoadingDialog(activity: BaseActivity) : BaseDialog(activity, R.layout.dialog_loading) {
    private var ivLoading: ImageView = contentView?.findViewById(R.id.iv_loading)!!
    private var animation: ObjectAnimator? = null
    override fun show() {
        if (animation == null) {
            animation = sortAnimation(ivLoading)
        }
        animation?.start()
        super.show()

    }

    override fun disMiss() {
        animation?.cancel()
        super.disMiss()
    }
    init {
        setCancelable(false)
    }


    protected fun sortAnimation(imageView: View?): ObjectAnimator {
        val objectAnimatorScale = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)
        objectAnimatorScale.duration = 1500
        objectAnimatorScale.repeatCount = -1
        return objectAnimatorScale

    }


}