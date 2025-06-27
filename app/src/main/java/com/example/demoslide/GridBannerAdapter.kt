package com.example.demoslide

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GridBannerAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<GridBannerAdapter.GridBannerViewHolder>() {

    companion object {
        private const val LOOP_MULTIPLIER = 1000
    }

    class GridBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageGridBanner: ImageView = itemView.findViewById(R.id.imageGridBanner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridBannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grid_banner, parent, false)
        return GridBannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridBannerViewHolder, position: Int) {
        val realPosition = position % imageUrls.size
        val url = imageUrls[realPosition]
        Glide.with(holder.imageGridBanner.context)
            .load(url)
            .centerCrop()
            .into(holder.imageGridBanner)

        // Set initial scale based on focus state
        holder.itemView.scaleX = if (holder.itemView.isFocused) 1.15f else 1.0f
        holder.itemView.scaleY = if (holder.itemView.isFocused) 1.15f else 1.0f

        // Add focus change animation and center on focus
        holder.itemView.setOnFocusChangeListener { v, hasFocus ->
            val scale = if (hasFocus) 1.15f else 1.0f
            v.animate().scaleX(scale).scaleY(scale).setDuration(200).start()
            if (hasFocus) {
                v.bringToFront()
                (v.parent as? RecyclerView)?.smoothScrollToPosition(holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = imageUrls.size * LOOP_MULTIPLIER

    fun getRealItemCount(): Int = imageUrls.size
} 