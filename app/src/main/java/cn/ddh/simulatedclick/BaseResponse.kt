package cn.ddh.simulatedclick

data class BaseResponse<T>(val code: Int, val msg: String, val time: String, val data: T?=null)
