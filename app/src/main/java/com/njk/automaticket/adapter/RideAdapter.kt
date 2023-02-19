package com.njk.automaticket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.njk.automaticket.databinding.CardRideBinding
import com.njk.automaticket.model.Ride

class RideAdapter(private val context: Context, private val dataset: List<Ride>) :
    RecyclerView.Adapter<RideAdapter.RideViewHolder>() {

    class RideViewHolder(private val binding: CardRideBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(ride: Ride){
            binding.ride = ride
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val binding = CardRideBinding.inflate(LayoutInflater.from(context))
        return RideViewHolder(binding)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        val ride = dataset[position]
        holder.bind(ride)
    }
}