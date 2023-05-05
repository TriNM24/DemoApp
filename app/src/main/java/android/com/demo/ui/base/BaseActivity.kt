package android.com.demo.ui.base

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

abstract class BaseActivity<BD : ViewDataBinding>: AppCompatActivity() {
    abstract val resourceLayoutId : Int
    var binding: BD? = null


    private lateinit var firebaseAnalytics: FirebaseAnalytics

    abstract fun onInitView(root: View?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}