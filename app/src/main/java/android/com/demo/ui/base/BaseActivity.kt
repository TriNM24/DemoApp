package android.com.demo.ui.base

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<BD : ViewDataBinding>: AppCompatActivity() {
    abstract val resourceLayoutId : Int
    var binding: BD? = null

    abstract fun onInitView(root: View?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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