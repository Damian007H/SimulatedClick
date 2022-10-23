package cn.ddh.simulatedclick.net

import cn.ddh.simulatedclick.BaseResponse
import cn.ddh.simulatedclick.Userinfo
import cn.ddh.simulatedclick.bean.AgentInfo
import cn.ddh.simulatedclick.bean.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @POST("agent/login")
    fun login(@Body user: Userinfo): Call<BaseResponse<LoginResponse>>

    @POST("agent/register")
    fun register(@Body user: Userinfo): Call<BaseResponse<LoginResponse>>


    /**
     * {
    "code": 1,
    "msg": "绑定一下成功",
    "time": "1653925667",
    "data": {
    "id": 3,
    "number": "12345",
    "product_id": 0,
    "batch": "",
    "agent_id": 0,
    "dealer_id": 0,
    "dealer_time": null,
    "find_num": 10,
    "first_time": "2022-05-14 01:28",
    "times": 1000,
    "user_id": 0,
    "result_msg": "恭喜，激活码绑定成功！",
    "result_view": "1",
    "product_view": "1",
    "agent_view": "1",
    "status": 2,
    "product": {
    "id": null,
    "name": null,
    "number": null,
    "bar_code": null,
    "image": null,
    "images": [],
    "content": null
    },
    "agent": {
    "id": null,
    "number": null,
    "nickname": null,
    "avatar": null,
    "mobile": null,
    "wechat": null,
    "province_id": null,
    "city_id": null,
    "area_id": null,
    "province_name": null,
    "city_name": null,
    "area_name": null,
    "address": null,
    "level": null,
    "parent_id": null,
    "logintime": null,
    "loginip": null,
    "times": null
    }
    }
    }
     */
    @POST("code/bindCode")
    fun bindCode(@Query("token") token: String, @Body user: Map<String, String>): Call<BaseResponse<AgentInfo>>


    @GET("agent/agentInfo")
    fun agentInfo(@Query("token") token: String): Call<BaseResponse<AgentInfo>>

}