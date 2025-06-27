package com.example.demoslide

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VerticalBannerAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<VerticalBannerAdapter.VerticalBannerViewHolder>() {

    companion object {
        private const val LOOP_MULTIPLIER = 1000
    }

    class VerticalBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageVerticalBanner: ImageView = itemView.findViewById(R.id.imageVerticalBanner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalBannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vertical_banner, parent, false)
        return VerticalBannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerticalBannerViewHolder, position: Int) {
        val realPosition = position % imageUrls.size
        val url = imageUrls[realPosition]
        Glide.with(holder.imageVerticalBanner.context)
            .load(url)
            .centerCrop()
            .into(holder.imageVerticalBanner)

        // Add 30dp vertical padding between items
        val paddingPx = (30 * Resources.getSystem().displayMetrics.density).toInt()
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = if (position == 0) paddingPx else paddingPx / 2
        layoutParams.bottomMargin = if (position == itemCount - 1) paddingPx else paddingPx / 2
        holder.itemView.layoutParams = layoutParams

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
} 