package com.deepwares.fishmarketplace.ui.notifications

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deepwares.fishmarketplace.R

class MyOrderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image = itemView.findViewById<ImageView>(R.id.image)
    val name = itemView.findViewById<TextView>(R.id.name)
    val kname = itemView.findViewById<TextView>(R.id.kname)
    val sizeType = itemView.findViewById<TextView>(R.id.size_type)
    val quantity = itemView.findViewById<TextView>(R.id.quantity)
    val cost = itemView.findViewById<TextView>(R.id.cost)
    val contact:Button = itemView.findViewById(R.id.contact)
    val time = itemView.findViewById<TextView>(R.id.time)
    val buyer:TextView = itemView.findViewById(R.id.seller)


}