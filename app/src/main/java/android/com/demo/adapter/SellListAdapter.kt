package android.com.demo.adapter

import android.com.demo.R
import android.com.demo.adapter.base.AnyAdapter
import android.com.demo.adapter.base.AnyViewHolder
import android.com.demo.adapter.viewholder.SellListViewHolder
import android.com.demo.data.room.entity.SellEntity
import android.com.demo.databinding.ItemSellBinding
import android.view.View

class SellListAdapter(private val clickHandler: ((callObject:SellEntity) -> Unit)? = null): AnyAdapter<SellEntity>() {
    override fun getLayoutId(): Int {
        return R.layout.item_sell
    }

    override fun onCreateViewHolder(view: View): AnyViewHolder<SellEntity> {
        return SellListViewHolder(ItemSellBinding.bind(view))
    }

    override fun onBindedViewHolder(holder: AnyViewHolder<SellEntity>, data: SellEntity) {
        when(holder){
            is SellListViewHolder -> holder.mBinding.root.setOnClickListener{
                clickHandler?.invoke(data)
            }
        }
    }
}