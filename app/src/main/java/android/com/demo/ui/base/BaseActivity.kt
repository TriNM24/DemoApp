package android.com.demo.ui.base

import android.Manifest
import android.com.demo.utils.hasPermission
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

abstract class BaseActivity<BD : ViewDataBinding>: AppCompatActivity() {
    abstract val resourceLayoutId : Int
    var binding: BD? = null


    lateinit var mFirebaseAnalytics: FirebaseAnalytics

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //Logger.d("Request Permission Granted")
            } else {
                //Logger.d("Request Permission Denied")
            }
        }

    abstract fun onInitView(root: View?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = Firebase.analytics
        //Firebase Analytics will auto log screen view event

        binding = DataBindingUtil.setContentView(this, resourceLayoutId)
        setContentView(binding?.root)
        onInitView(binding?.root)
    }

    fun updateTitle(title:String){
        Handler(mainLooper).post {
            supportActionBar?.title = title
        }
    }

    fun showHideActionBar(isShow: Boolean) {
        Handler(mainLooper).post {
            if (isShow) {
                supportActionBar?.show()
            } else {
                supportActionBar?.hide()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!this.hasPermission(
                    Array(1) { Manifest.permission.POST_NOTIFICATIONS })
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}