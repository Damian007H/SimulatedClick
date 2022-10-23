package cn.ddh.simulatedclick

import java.io.Serializable

data class Userinfo(var mobile: String? = null, var password: String? = null, val id: Int? = null, val number: String? = null, val nickname: String? = null, val token: String? = null, val times: Int? = 0) : Serializable
