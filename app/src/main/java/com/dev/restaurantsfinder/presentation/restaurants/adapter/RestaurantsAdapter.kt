package com.dev.restaurantsfinder.presentation.restaurants.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.restaurantsfinder.R
import com.dev.restaurantsfinder.databinding.ItemRestaurantsBinding
import com.dev.restaurantsfinder.domain.model.restaurants.RestaurantDataModel

class RestaurantAdapter(private var items: ArrayList<RestaurantDataModel> = arrayListOf(),
                        private val listener: OnItemClick?=null) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(val binding: ItemRestaurantsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ItemRestaurantsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = items[position]
        holder.binding.apply {
            tvName.text = restaurant.name
            tvAddress.text = restaurant.distance.toString()
//            tvStatus.text = restaurant.status
            tvRating.text = restaurant.rating.toString()

            Glide.with(root.context)
                .load(restaurant.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivRestaurant)
        }
    }

    override fun getItemCount(): Int = items.size
    interface OnItemClick{
        fun onItemClick(position: Int){}
    }

}
