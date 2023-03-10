package android.com.demo.ui.base

import android.app.Dialog
import android.com.demo.utils.DialogUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<BD : ViewDataBinding, VM : ViewModel> : Fragment() {
    lateinit var viewModel: VM
    var binding: BD? = null
    lateinit var mLoadingDialog: Dialog

    abstract val resourceLayoutId: Int
    abstract fun onInitView(root: View?)
    protected abstract fun subscribeUi(viewModel: VM)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(resourceLayoutId, container, false)
        binding = DataBindingUtil.bind(root)
        initViewModel()
        onInitView(root)
        return root
    }

    fun updateTitle(title: String) {
        (activity as BaseActivity<*>)?.updateTitle(title)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDialog = DialogUtils.getLoading(requireContext())
        subscribeUi(viewModel)
    }

    open val owner: ViewModelStoreOwner
        get() = this

    private fun initViewModel() {
        viewModel = ViewModelProvider(owner)[clazz]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    open fun handleOnBackPressed(){
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    /////////////////////
    // support fun
    /////////////////////////
    //Get the actual type of generic T
    @Suppress("UNCHECKED_CAST")
    private val clazz: Class<VM> =
        (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>
}