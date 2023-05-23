package android.com.demo.ui.mainMenu

import android.com.demo.R
import android.com.demo.databinding.FragmentMainMenuBinding
import android.com.demo.ui.base.BaseFragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuFragment : BaseFragment<FragmentMainMenuBinding, MainMenuViewModel>(),
    MainMenuActions {
    override val resourceLayoutId: Int
        get() = R.layout.fragment_main_menu

    lateinit var mNavController: NavController

    override fun onInitView(root: View?) {
        mNavController = findNavController()
    }

    override fun subscribeUi(viewModel: MainMenuViewModel) {
        binding?.viewModel = viewModel
        binding?.actions = this
    }

    override fun onClickCallButton() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            param(FirebaseAnalytics.Param.ITEM_NAME, "onClickCallButton")
        }

        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_callListFragment)
        }
    }

    override fun onClickBuyButton() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            param(FirebaseAnalytics.Param.ITEM_NAME, "onClickBuyButton")
        }
        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_buyListFragment)
        }
    }

    override fun onClickSellButton() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            param(FirebaseAnalytics.Param.ITEM_NAME, "onClickSellButton")
        }
        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_sellListFragment)
        }
    }

    override fun onClickCrashButton() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            param(FirebaseAnalytics.Param.ITEM_NAME, "onClickCrashButton")
        }
        val temp = 1/0
    }
}