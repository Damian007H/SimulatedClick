package cn.ddh.simulatedclick.activity.regiester

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import cn.ddh.simulatedclick.R
import cn.ddh.simulatedclick.Userinfo
import cn.ddh.simulatedclick.activity.BaseActivity
import cn.ddh.simulatedclick.databinding.ActivityRegisterBinding


class RegisterActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var vm: RegisterVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //获取布局绑定实例
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register) //获取VM实例 //获取VM实例
        vm = ViewModelProvider(this, AndroidViewModelFactory(application)).get(RegisterVM::class.java) //把他们邦在一起 //把他们邦在一起
        binding.vm = vm //设置VM所使用的生命周期
        //设置VM所使用的生命周期
        binding.lifecycleOwner = this
        vm.setBinding(binding, this)

        binding.btLogin.setOnClickListener(this)

        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                checkLoginBtnStatus()
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                checkLoginBtnStatus()
            }
        })
        checkLoginBtnStatus()
    }

    private fun checkLoginBtnStatus() {
        val account = getAccount()
        val password = getPassword()
        binding.btLogin.isEnabled = !(account == "" || password == "")

    }

    private fun getPassword(): String = binding.etPassword.text.toString().trim()

    private fun getAccount(): String = binding.etUsername.text.toString().trim()
    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.bt_login -> {
                    val userName = getAccount()
                    val password = getPassword()

                    if (userName == "" || password == "") {
                        showToast("请填写用户名或密码")
                        return
                    }
                    vm.sendRequest(userName, password)

                }
                else -> {

                }
            }
        }
    }

    fun onRegisterSuccess(userinfo: Userinfo?) {
        setResult(RESULT_OK, Intent().putExtra("userInfo", userinfo))
        finish()

    }


}