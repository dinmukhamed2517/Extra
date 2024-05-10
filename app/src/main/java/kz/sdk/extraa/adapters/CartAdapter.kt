package kz.sdk.extraa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kz.sdk.extraa.R
import kz.sdk.extraa.base.BaseProductViewHolder
import kz.sdk.extraa.databinding.ItemCartBinding
import kz.sdk.extraa.models.Product


class CartAdapter: ListAdapter<Product, BaseProductViewHolder<*>>(ProductDiffUtils()) {

    var itemClick:((Product) ->Unit)? = null

    var deleteButtonClicked: ((Product)->Unit)? = null

    class ProductDiffUtils: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseProductViewHolder<*> {
        return ProductViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseProductViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class ProductViewHolder(binding:ItemCartBinding): BaseProductViewHolder<ItemCartBinding>(binding){
        override fun bindView(item: Product) {
            with(binding){
                Glide.with(itemView.context)
                    .load(item.img)
                    .placeholder(R.drawable.placeholder_event)
                    .into(img)
                title.text = item.title
                price.text = item.calories.toString()+" Калорий"
                deleteBtn.setOnClickListener {
                    deleteButtonClicked?.invoke(item)
                }
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }
}
