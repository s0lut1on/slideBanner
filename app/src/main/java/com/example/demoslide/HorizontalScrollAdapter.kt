package com.example.demoslide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HorizontalScrollAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<HorizontalScrollAdapter.HorizontalScrollViewHolder>() {

    companion object {
        private const val LOOP_MULTIPLIER = 1000
        private const val FOCUS_SCALE = 1.15f
        private const val ANIMATION_DURATION = 200L
    }

    class HorizontalScrollViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageHorizontal)

        fun setFocusState(hasFocus: Boolean) {
            val scale = if (hasFocus) FOCUS_SCALE else 1.0f
            itemView.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(ANIMATION_DURATION)
                .start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalScrollViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal_scroll, parent, false)
        return HorizontalScrollViewHolder(view).apply {
            // Set initial focus state
            itemView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                setFocusState(hasFocus)
                if (hasFocus) {
                    v.bringToFront()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: HorizontalScrollViewHolder, position: Int) {
        val realPosition = position % imageUrls.size
        val url = imageUrls[realPosition]
        
        // Load image
        Glide.with(holder.imageView.context)
            .load(url)
            .centerCrop()
            .into(holder.imageView)

        // Set initial scale based on focus state
        holder.setFocusState(holder.itemView.isFocused)

        // Make sure the item is focusable
        holder.itemView.isFocusable = true
        holder.itemView.isFocusableInTouchMode = true
    }

    override fun getItemCount(): Int = imageUrls.size * LOOP_MULTIPLIER

    fun getRealItemCount(): Int = imageUrls.size
}