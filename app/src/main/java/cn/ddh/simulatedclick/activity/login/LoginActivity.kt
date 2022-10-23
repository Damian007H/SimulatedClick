package cn.ddh.simulatedclick.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import cn.ddh.simulatedclick.App
import cn.ddh.simulatedclick.R
import cn.ddh.simulatedclick.Userinfo
import cn.ddh.simulatedclick.activity.BaseActivity
import cn.ddh.simulatedclick.activity.home.HomeActivity
import cn.ddh.simulatedclick.activity.regiester.RegisterActivity
import cn.ddh.simulatedclick.databinding.ActivityLoginBinding
import com.orhanobut.hawk.Hawk


class LoginActivity : BaseActivity(), View.OnClickListener, TextWatcher {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var vm: LoginVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //获取布局绑定实例
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login) //获取VM实例 //获取VM实例
        vm = ViewModelProvider(this, AndroidViewModelFactory(application)).get(LoginVM::class.java) //把他们邦在一起 //把他们邦在一起
        binding.vm = vm //设置VM所使用的生命周期
        //设置VM所使用的生命周期
        binding.lifecycleOwner = this
        vm.setBinding(binding, this)

        binding.btLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
//        binding.etUsername.
        binding.etUsername.addTextChangedListener(this)
        binding.etPassword.addTextChangedListener(this)
        checkLoginBtnStatus()

        val userinfo = Hawk.get<Userinfo>("key_userinfo")
        if (userinfo != null) {
            onLoginSuccess(userinfo)
        }


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
                R.id.tv_register -> {
                    startActivityForResult(Intent(this, RegisterActivity::class.java), 234)
                }
                else -> {

                }
            }
        }
    }

    fun onLoginSuccess(userinfo: Userinfo) {
        Hawk.put("key_userinfo", userinfo)
        App.INSTANCE.currentUserinfo = userinfo
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 234 && data != null) {
            val serializableExtra = data.getSerializableExtra("userInfo")
            serializableExtra?.let {
                onLoginSuccess(it as Userinfo)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        checkLoginBtnStatus()
    }


}