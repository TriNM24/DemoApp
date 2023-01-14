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
        viewModel.resultChecking.observe(this) {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
        viewModel.processChecking()
    }
}