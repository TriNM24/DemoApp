package android.com.demo.ui.splash

import android.com.demo.ui.main.MainActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //prevent start new activity when click launcher
        //https://stackoverflow.com/questions/19545889/app-restarts-rather-than-resumes
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null
            && intent.action.equals(Intent.ACTION_MAIN)) {
            finish()
            return
        }

        viewModel.resultChecking.observe(this) {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()
        }
        viewModel.processChecking()
    }
}