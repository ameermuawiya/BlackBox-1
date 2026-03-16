package top.niunaijun.blackboxa.app.rocker

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.imuxuan.floatingview.FloatingMagnetView
import com.imuxuan.floatingview.FloatingView
import top.niunaijun.blackbox.entity.location.BLocation
import top.niunaijun.blackbox.fake.frameworks.BLocationManager
import top.niunaijun.blackboxa.app.App
import top.niunaijun.blackboxa.widget.EnFloatView
import kotlin.math.cos
import kotlin.math.sin

object RockerManager {

    private const val TAG = "RockerManager"

    private const val Ea = 6378137     // Equatorial radius  

    private const val Eb = 6356725     // Polar radius 

    fun init(application: Application?, userId: Int) {

        if (application == null || !BLocationManager.isFakeLocationEnable()) {
            return
        }

        val enFloatView = initFloatView()

        if (enFloatView is EnFloatView) {
            enFloatView.setListener { angle: Float, distance: Float ->
                changeLocation(distance, angle, application.packageName, userId)
            }
        }

        application.registerActivityLifecycleCallbacks(object : BaseActivityLifecycleCallback {

            override fun onActivityStarted(activity: Activity) {
                super.onActivityStarted(activity)
                FloatingView.get().attach(activity)
            }

            override fun onActivityStopped(activity: Activity) {
                super.onActivityStopped(activity)
                FloatingView.get().detach(activity)
            }

            /*
             * Override the method with correct signature
             */
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                // Do nothing
            }
        })
    }

    private fun initFloatView(): FloatingMagnetView? {
        val params = FrameLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        params.gravity = Gravity.START or Gravity.CENTER
        val view = EnFloatView(App.getContext())
        view.layoutParams = params

        FloatingView.get().customView(view)

        return FloatingView.get().view
    }

    private fun changeLocation(distance: Float, angle: Float, packageName: String, userId: Int) {
        val location = BLocationManager.get().getLocation(userId, packageName)

        val dx = distance  * sin(angle * Math.PI / 180.0)
        val dy = distance  * cos(angle * Math.PI / 180.0)

        val ec = Eb + (Ea - Eb) * (90.0 - location.latitude) / 90.0
        val ed = ec * cos(location.latitude * Math.PI / 180)

        val newLng = (dx / ed + location.longitude * Math.PI / 180.0) * 180.0 / Math.PI

        val newLat = (dy / ec + location.latitude * Math.PI / 180.0) * 180.0 / Math.PI
        val newLocation = BLocation(newLat, newLng)

        BLocationManager.get().setLocation(userId, packageName, newLocation)
    }
}
