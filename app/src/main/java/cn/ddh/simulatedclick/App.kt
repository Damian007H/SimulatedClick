package cn.ddh.simulatedclick

import android.app.Application
import android.content.Context
import com.orhanobut.hawk.Hawk
import com.tencent.bugly.crashreport.CrashReport

class App : Application() {
    object INSTANCE {
        var context: Context? = null
        var currentUserinfo: Userinfo? = null
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE.context = this
        Hawk.init(this).build()

        CrashReport.initCrashReport(applicationContext, "f3da4fbbfb", true);


    }

}