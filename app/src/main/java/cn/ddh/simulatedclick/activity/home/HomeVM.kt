package cn.ddh.simulatedclick.activity.home

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import cn.ddh.simulatedclick.App
import cn.ddh.simulatedclick.BaseCallback
import cn.ddh.simulatedclick.bean.AgentInfo
import cn.ddh.simulatedclick.bean.LoginResponse
import cn.ddh.simulatedclick.databinding.ActivityHomeBinding
import cn.ddh.simulatedclick.net.RetrofitRequest

class HomeVM(application: Application) : AndroidViewModel(application) {
    private var activityHomeBinding: ActivityHomeBinding? = null
    private var homeActivity: HomeActivity? = null


    fun setBinding(binding: ActivityHomeBinding, activity: HomeActivity) {
        this.activityHomeBinding = binding
        this.homeActivity = activity
    }

    fun submit(content: String) {
        val map = mutableMapOf<String, String>()
        map["code"] = content
        map["platform"] = "android"

        val token = App.INSTANCE.currentUserinfo?.token
        if (TextUtils.isEmpty(token)) {
            homeActivity?.showToast("请先登录")
            return
        }
        RetrofitRequest.getInstance().bindCode(token!!, map).enqueue(object : BaseCallback<AgentInfo> {
            override fun onNext(data: AgentInfo?) {
                homeActivity?.showToast(if (data != null) "绑定成功，剩余次数${data.times}" else "绑定失败")
            }
        })

    }


}


