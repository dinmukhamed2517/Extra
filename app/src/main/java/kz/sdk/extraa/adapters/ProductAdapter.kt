package kz.sdk.extraa.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kz.sdk.extraa.R
import kz.sdk.extraa.base.BaseProductViewHolder
import kz.sdk.extraa.databinding.ItemProductBinding
import kz.sdk.extraa.models.Product

class ProductAdapter: ListAdapter<Product, BaseProductViewHolder<*>>(ProductDiffUtils()) {

    class ProductDiffUtils: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    var itemClick:((Product) ->Unit)? = null

    inner class ProductViewHolder(binding:ItemProductBinding):
        BaseProductViewHolder<ItemProductBinding>(binding){
        override fun bindView(item: Product) {
            with(binding){
                Glide.with(itemView.context)
                    .load(item.img)
                    .placeholder(R.drawable.placeholder_event)
                    .into(imageView)
                title.text = item.title
                price.text = item.calories.toString()+" Калорий"
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseProductViewHolder<*> {
        return ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseProductViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }
}
