package android.com.demo.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.com.demo.utils.DialogUtils
import android.com.demo.utils.hideKeyboard
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<BD : ViewDataBinding, VM : ViewModel> : Fragment() {
    lateinit var viewModel: VM
    var binding: BD? = null
    lateinit var mLoadingDialog: Dialog

    lateinit var firebaseAnalytics: FirebaseAnalytics

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
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        showHideActionBar(mIsShowActionBar)
        return root
    }

    fun updateTitle(title: String) {
        (activity as BaseActivity<*>)?.updateTitle(title)
    }

    private fun showHideActionBar(isShow: Boolean) {
        (activity as BaseActivity<*>).showHideActionBar(isShow)
    }

    @SuppressLint("ClickableViewAccessibility")
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDialog = DialogUtils.getLoading(requireContext())
        binding?.root?.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    hideKeyboard()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
        subscribeUi(viewModel)
    }

    open val mIsShowActionBar: Boolean
        get() = true

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