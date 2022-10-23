package cn.ddh.simulatedclick.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.appcompat.app.AppCompatActivity

class ClickBroadcast(var activity: BaseActivity, val requestCode: Int, var mProjectionManager: MediaProjectionManager?) : BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {
        getMediaProjectionManger()
    }

    private fun getMediaProjectionManger() {

        if (mProjectionManager != null) {
        }

    }

}


