package cn.ddh.simulatedclick

import cn.ddh.simulatedclick.zxing.utils.ActivityManager
import cn.ddh.simulatedclick.zxing.utils.ToastUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface BaseCallback<T : Any> : Callback<BaseResponse<T>> {

    override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
        if (response.code() == 200) {
            val body = response.body()

            if (body != null) {
                when (body.code) {
                    0 -> onFail(body.msg)
                    1 -> onNext(body.data)
                    -1 -> {
                        ActivityManager.getInstance().exit()
                    }
                    else -> onFail("没有对应的Code")
                }
            } else {
                onFail("数据返回为空")
            }
        } else {
            onFail("请求异常，code:${response.code()};url:${call.request().url()}")
        }


    }

    override fun onFailure(call: Call<BaseResponse<T>>, t: Throwable) {
        onFail("请求失败${call.request().url()}")
    }


    fun onNext(data: T?) {
        onComplete()
    }

    fun onFail(msg: String) {
        ToastUtil.show(msg)
        onComplete()
    }

    fun onComplete() {

    }


}