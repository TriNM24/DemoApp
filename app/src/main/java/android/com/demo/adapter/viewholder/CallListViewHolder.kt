package android.com.demo.adapter.viewholder

import android.com.demo.adapter.base.AnyViewHolder
import android.com.demo.data.api.response.CallObjectResponse
import android.com.demo.databinding.ItemCallBinding

class CallListViewHolder(val mBinding: ItemCallBinding) : AnyViewHolder<CallObjectResponse>(mBinding.root) {
    override fun bind(data: CallObjectResponse) {
        mBinding.data = data
    }
}