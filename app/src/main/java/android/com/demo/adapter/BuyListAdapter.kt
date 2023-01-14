package android.com.demo.adapter

import android.com.demo.R
import android.com.demo.adapter.base.AnyAdapter
import android.com.demo.adapter.base.AnyViewHolder
import android.com.demo.adapter.viewholder.BuyListViewHolder
import android.com.demo.data.api.response.BuyObjectResponse
import android.com.demo.databinding.ItemBuyBinding
import android.view.View

class BuyListAdapter(private val clickHandler: ((callObject:BuyObjectResponse) -> Unit)? = null): AnyAdapter<BuyObjectResponse>() {
    override fun getLayoutId(): Int {
        return R.layout.item_buy
    }

    override fun onCreateViewHolder(view: View): AnyViewHolder<BuyObjectResponse> {
        return BuyListViewHolder(ItemBuyBinding.bind(view))
    }

    override fun onBindedViewHolder(holder: AnyViewHolder<BuyObjectResponse>, data: BuyObjectResponse) {
        when(holder){
            is BuyListViewHolder -> holder.mBinding.root.setOnClickListener{
                clickHandler?.invoke(data)
            }
        }
    }
}