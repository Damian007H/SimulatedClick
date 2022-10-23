package cn.ddh.simulatedclick.activity.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cn.ddh.simulatedclick.BaseCallback
import cn.ddh.simulatedclick.Userinfo
import cn.ddh.simulatedclick.bean.LoginResponse
import cn.ddh.simulatedclick.databinding.ActivityLoginBinding
import cn.ddh.simulatedclick.net.RetrofitRequest


class LoginVM(application: Application) : AndroidViewModel(application) {

    private var binding: ActivityLoginBinding? = null
    private var loginActivity: LoginActivity? = null


    fun setBinding(binding: ActivityLoginBinding, mainActivity: LoginActivity) {
        this.binding = binding
        this.loginActivity = mainActivity

    }

    fun sendRequest(userName: String, password: String) {
        val userinfo = Userinfo()
        userinfo.password = password
        userinfo.mobile = userName
        loginActivity?.showLoading()
        RetrofitRequest.getInstance().login(userinfo).enqueue(object : BaseCallback<LoginResponse> {

            override fun onNext(data: LoginResponse?) {
                loginActivity?.let {
                    if (data?.userinfo != null) {
                        it.onLoginSuccess(data.userinfo)
                    } else {
                        it.showToast("登录失败")
                    }
                }

            }

            override fun onComplete() {
                loginActivity?.dismissLoading()
            }


        })


    }

}