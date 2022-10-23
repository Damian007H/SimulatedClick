package cn.ddh.simulatedclick.zxing.utils;


import android.content.Intent;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import cn.ddh.simulatedclick.App;
import cn.ddh.simulatedclick.activity.BaseActivity;
import cn.ddh.simulatedclick.activity.login.LoginActivity;

public class ActivityManager {
    private volatile static ActivityManager instance;


    private final List<BaseActivity> activityList = new ArrayList<>();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    public void addActivity(BaseActivity baseMVPActivity) {
        activityList.add(baseMVPActivity);
    }

    public void removeActivity(BaseActivity baseMVPActivity) {
        activityList.remove(baseMVPActivity);
    }


    /**
     * 退出所有activity 回到首页
     */
    public void exit() {
        for (BaseActivity activity : activityList) {
            activity.finish();
        }
        Hawk.delete("key_userinfo");
        App.INSTANCE.INSTANCE.setCurrentUserinfo(null);

        Intent intent = new Intent(App.INSTANCE.INSTANCE.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.INSTANCE.INSTANCE.getContext().startActivity(intent);
    }


}