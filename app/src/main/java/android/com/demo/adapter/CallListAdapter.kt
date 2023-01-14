package android.com.demo.adapter

import android.com.demo.R
import android.com.demo.adapter.base.AnyAdapter
import android.com.demo.adapter.base.AnyViewHolder
import android.com.demo.adapter.viewholder.CallListViewHolder
import android.com.demo.data.api.response.CallObjectResponse
import android.com.demo.databinding.ItemCallBinding
import android.view.View

class CallListAdapter(private val clickHandler: ((callObject:CallObjectResponse) -> Unit)? = null): AnyAdapter<CallObjectResponse>() {
    override fun getLayoutId(): Int {
        return R.layout.item_call
    }

    override fun onCreateViewHolder(view: View): AnyViewHolder<CallObjectResponse> {
        return CallListViewHolder(ItemCallBinding.bind(view))
    }

    override fun onBindedViewHolder(holder: AnyViewHolder<CallObjectResponse>, data: CallObjectResponse) {
        when(holder){
            is CallListViewHolder -> holder.mBinding.root.setOnClickListener{
                clickHandler?.invoke(data)
            }
        }
    }
}