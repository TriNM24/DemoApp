package android.com.demo.adapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AnyViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: T)
}