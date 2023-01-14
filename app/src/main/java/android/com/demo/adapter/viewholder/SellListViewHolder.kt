package android.com.demo.adapter.viewholder

import android.com.demo.adapter.base.AnyViewHolder
import android.com.demo.data.room.entity.SellEntity
import android.com.demo.databinding.ItemSellBinding

class SellListViewHolder(val mBinding: ItemSellBinding) : AnyViewHolder<SellEntity>(mBinding.root) {
    override fun bind(data: SellEntity) {
        mBinding.data = data
    }
}