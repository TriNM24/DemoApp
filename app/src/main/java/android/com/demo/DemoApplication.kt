package android.com.demo

import android.app.Application
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(MyDebugTree())
        }

        val crashlytics = Firebase.crashlytics
        //disable crashlytics for debug
        crashlytics.setCrashlyticsCollectionEnabled(BuildConfig.DEBUG)
        //add custom key for more information
        crashlytics.setCustomKeys {
            key("environment", BuildConfig.BUILD_TYPE)
            key("version code", BuildConfig.VERSION_CODE)
            key("version name", BuildConfig.VERSION_NAME)
        }
        Firebase.analytics.setUserId("userid_test")
    }
}

class MyDebugTree : Timber.DebugTree() {
    private val TAG = "LogTag"
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        //prevent tag maximum length -> add tag to message
        super.log(priority, TAG ,"$message - $tag", t)
    }
}