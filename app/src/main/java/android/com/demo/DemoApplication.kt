package android.com.demo

import android.app.Application
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