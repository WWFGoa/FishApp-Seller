package com.deepwares.fishmarketplace.ui.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deepwares.fishmarketplace.R
import kotlinx.android.synthetic.main.order_item.view.*

class OrderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image = itemView.findViewById<ImageView>(R.id.image)
    val name = itemView.findViewById<TextView>(R.id.name)
    val kname = itemView.findViewById<TextView>(R.id.kname)
    val quantity = itemView.findViewById<TextView>(R.id.quantity)
    val cost = itemView.findViewById<TextView>(R.id.cost)
    val size: TextView = itemView.findViewById(R.id.size_type)
    val soldOut: TextView = itemView.findViewById(R.id.sold_out)
    val expired: TextView = itemView.findViewById(R.id.expired)
    val soldOutOverlay: View = itemView.findViewById(R.id.sold_out_overlay)
    val time = itemView.findViewById<TextView>(R.id.time)
    val catchLocation = itemView.findViewById<TextView>(R.id.location)
}