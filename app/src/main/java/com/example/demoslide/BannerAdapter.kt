package com.example.demoslide

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BannerAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    companion object {
        private const val LOOP_MULTIPLIER = 1000
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageBanner: ImageView = itemView.findViewById(R.id.imageBanner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val realPosition = position % imageUrls.size
        val url = imageUrls[realPosition]
        Glide.with(holder.imageBanner.context)
            .load(url)
            .centerCrop()
            .into(holder.imageBanner)

        // Add 30dp horizontal padding between items
        val paddingPx = (30 * Resources.getSystem().displayMetrics.density).toInt()
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.leftMargin = if (position == 0) paddingPx else paddingPx / 2
        layoutParams.rightMargin = if (position == itemCount - 1) paddingPx else paddingPx / 2
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