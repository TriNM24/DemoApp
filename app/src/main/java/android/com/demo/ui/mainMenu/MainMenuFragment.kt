package android.com.demo.ui.mainMenu

import android.com.demo.R
import android.com.demo.databinding.FragmentMainMenuBinding
import android.com.demo.ui.base.BaseFragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
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
        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_callListFragment)
        }
    }

    override fun onClickBuyButton() {
        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_buyListFragment)
        }
    }

    override fun onClickSellButton() {
        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_sellListFragment)
        }
    }
}