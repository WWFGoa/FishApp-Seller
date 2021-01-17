package com.deepwares.fishmarketplace.ui.tutorial

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deepwares.fishmarketplace.R

class TutorialVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image)
    val desc: TextView = itemView.findViewById(R.id.desc)
    val continueButton: Button = itemView.findViewById(R.id.continueButton)
}