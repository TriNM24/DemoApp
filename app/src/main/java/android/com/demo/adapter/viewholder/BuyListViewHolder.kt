package android.com.demo.adapter.viewholder

import android.com.demo.adapter.base.AnyViewHolder
import android.com.demo.data.api.response.BuyObjectResponse
import android.com.demo.databinding.ItemBuyBinding

class BuyListViewHolder(val mBinding: ItemBuyBinding) : AnyViewHolder<BuyObjectResponse>(mBinding.root) {
    override fun bind(data: BuyObjectResponse) {
        mBinding.data = data
    }
}