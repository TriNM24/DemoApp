package android.com.demo.ui.mainMenu.buyList

import android.com.demo.R
import android.com.demo.adapter.BuyListAdapter
import android.com.demo.data.api.Status
import android.com.demo.databinding.FragmentListBinding
import android.com.demo.ui.base.BaseFragment
import android.com.demo.utils.DialogUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyListFragment : BaseFragment<FragmentListBinding, BuyListViewModel>(), MenuProvider {
    override val resourceLayoutId: Int
        get() = R.layout.fragment_list

    private lateinit var adapter: BuyListAdapter

    override fun onInitView(root: View?) {
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter = BuyListAdapter {
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.divider)
            ?.let { dividerItemDecoration.setDrawable(it) }
        binding?.listMenu?.addItemDecoration(dividerItemDecoration)
        binding?.listMenu?.adapter = adapter
    }

    override fun subscribeUi(viewModel: BuyListViewModel) {
        viewModel.mDataList.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    mLoadingDialog.dismiss()
                    result?.data?.let {
                        adapter.setData(it)
                        adapter.notifyItemRangeChanged(0, it.size)
                    }
                }
                Status.ERROR -> {
                    mLoadingDialog.dismiss()
                    DialogUtils.showAlertDialog(requireContext(), result.message)
                }
                Status.LOADING -> {
                    mLoadingDialog.show()
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_buy, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_buy -> {
                Toast.makeText(requireContext(), "Buy", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }
}