package android.com.demo.adapter.base
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class AnyAdapter<T> : RecyclerView.Adapter<AnyViewHolder<T>>() {

    private var items: List<T>? = null

    abstract fun getLayoutId(): Int

    fun setData(data: List<T>){
        items = data
    }

    abstract fun onCreateViewHolder(view: View): AnyViewHolder<T>

    abstract fun onBindedViewHolder(holder: AnyViewHolder<T>, data: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnyViewHolder<T> = onCreateViewHolder(
        LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
    )

    override fun getItemCount(): Int = items?.size?:0

    override fun onBindViewHolder(holder: AnyViewHolder<T>, position: Int) {
        items?.let {
            holder.bind(it[position])
            onBindedViewHolder(holder, it[position])
        }
    }

}