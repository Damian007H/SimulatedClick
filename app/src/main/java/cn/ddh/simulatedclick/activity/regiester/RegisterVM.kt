package cn.ddh.simulatedclick.activity.regiester

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cn.ddh.simulatedclick.BaseCallback
import cn.ddh.simulatedclick.Userinfo
import cn.ddh.simulatedclick.bean.LoginResponse
import cn.ddh.simulatedclick.databinding.ActivityLoginBinding
import cn.ddh.simulatedclick.databinding.ActivityRegisterBinding
import cn.ddh.simulatedclick.net.RetrofitRequest


class RegisterVM(application: Application) : AndroidViewModel(application) {

    private var binding: ActivityRegisterBinding? = null
    private var loginActivity: RegisterActivity? = null


    fun setBinding(binding: ActivityRegisterBinding, mainActivity: RegisterActivity) {
        this.binding = binding
        this.loginActivity = mainActivity

    }

    fun sendRequest(userName: String, password: String) {
        val userinfo = Userinfo()
        userinfo.password = password
        userinfo.mobile = userName
        RetrofitRequest.getInstance().register(userinfo).enqueue(object : BaseCallback<LoginResponse> {

            override fun onNext(data: LoginResponse?) {
                loginActivity?.let {
                    if (data != null) {
                        it.onRegisterSuccess(data.userinfo)
                    } else {
                        it.showToast("注册失败")
                    }
                }
            }

        })


    }

}

