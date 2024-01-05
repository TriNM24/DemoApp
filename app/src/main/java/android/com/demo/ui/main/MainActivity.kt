package android.com.demo.ui.main

import android.com.demo.R
import android.com.demo.databinding.ActivityMainBinding
import android.com.demo.ui.base.BaseActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val resourceLayoutId: Int
        get() = R.layout.activity_main

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding?.toolbar)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        //list of destination do not show back button
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.loginFragment)
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()

        val appInstanceID = mFirebaseAnalytics.appInstanceId.addOnCompleteListener {
            Log.d("testt","appInstanceID: ${it.result}")
        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onInitView(root: View?) {

    }

    /*
    Handle back button
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}