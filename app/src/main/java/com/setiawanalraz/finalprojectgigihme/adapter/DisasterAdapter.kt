package com.setiawanalraz.finalprojectgigihme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.setiawanalraz.finalprojectgigihme.R
import com.setiawanalraz.finalprojectgigihme.api.model.DisasterReports
import com.setiawanalraz.finalprojectgigihme.databinding.ItemLayoutBinding

class DisasterAdapter(private var disasters: List<DisasterReports>) :
    RecyclerView.Adapter<DisasterAdapter.DisasterViewHolder>() {
    inner class DisasterViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DisasterAdapter.DisasterViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DisasterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisasterAdapter.DisasterViewHolder, position: Int) {
        with(holder) {
            val data = disasters[position]
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.default_image)
                            .error(R.drawable.default_image)
                            .override(250, 250)
                            .centerCrop()
                    ).into(ivPhoto)
                tvTitle.text = data.disasterType
                tvDescription.text = data.text
            }
        }
    }

    override fun getItemCount(): Int {
        return disasters.size
    }

}