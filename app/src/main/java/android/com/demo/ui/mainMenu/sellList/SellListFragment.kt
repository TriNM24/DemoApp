package android.com.demo.ui.mainMenu.sellList

import android.com.demo.R
import android.com.demo.adapter.SellListAdapter
import android.com.demo.data.api.Status
import android.com.demo.databinding.FragmentListBinding
import android.com.demo.ui.base.BaseFragment
import android.com.demo.utils.DialogUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellListFragment : BaseFragment<FragmentListBinding, SellListViewModel>() {
    override val resourceLayoutId: Int
        get() = R.layout.fragment_list

    private lateinit var adapter: SellListAdapter
    override fun onInitView(root: View?) {
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter = SellListAdapter {
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.divider)
            ?.let { dividerItemDecoration.setDrawable(it) }
        binding?.listMenu?.addItemDecoration(dividerItemDecoration)
        binding?.listMenu?.adapter = adapter
    }

    override fun subscribeUi(viewModel: SellListViewModel) {
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
    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }
}