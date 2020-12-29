package com.deepwares.fishmarketplace.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.datastore.generated.model.Inventory
import com.deepwares.fishmarketplace.App
import com.deepwares.fishmarketplace.R
import com.deepwares.fishmarketplace.model.FishRepository
import com.deepwares.fishmarketplace.model.Species

class OrderAdapter : RecyclerView.Adapter<OrderVH>() {
    val species = ArrayList<Species>().apply { FishRepository.species }
    val items = ArrayList<Inventory>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderVH {
        val vh =
            OrderVH(LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OrderVH, position: Int) {
        val item = items[position]
        val spec =
            FishRepository.species[if (item.species >= FishRepository.species.size) 0 else item.species]//item.species
        holder.image.setImageResource(spec.image)
        holder.name.setText(spec.name)

        holder.cost.setText(
            App.INSTANCE.getString(R.string.price_in_kg, item.price.toString())
        )
        holder.quantity.setText(
            App.INSTANCE.getString(
                R.string.qty_in_kg,
                item.availableQuantity.toString()
            )
        )

    }

}