package com.deepwares.fishmarketplace.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.datastore.generated.model.Inventory
import com.deepwares.fishmarketplace.App
import com.deepwares.fishmarketplace.R
import com.deepwares.fishmarketplace.model.FishRepository
import com.deepwares.fishmarketplace.model.Species
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class OrderAdapter : RecyclerView.Adapter<OrderVH>() {
    val species = ArrayList<Species>().apply { FishRepository.species }
    val items = ArrayList<Inventory>()
    val dateTimeFormatter = DateTimeFormat.shortDateTime()
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

        holder.size.text = item.size.name

        val createdAt = item.createdAt.toDate()
        val expired = createdAt.before(DateTime.now().minusHours(6).toDate())

        val available = item.availableQuantity > 0f
        holder.soldOut.visibility = if (!available || expired) View.VISIBLE else View.GONE
        holder.soldOutOverlay.visibility = if (!available || expired) View.VISIBLE else View.GONE

        holder.soldOut.text =
            holder.itemView.resources.getString(if (expired) R.string.expired else R.string.sold_out)
        (holder.itemView as CardView).foreground = if (!available || expired) App.getInstance()
            .getDrawable(R.color.black_5_pct) else App.getInstance()
            .getDrawable(R.color.transparent)

        val time = item.createdAt.format()
        val dateTime = DateTime.parse(time)
        holder.time.setText(dateTimeFormatter.print(dateTime))
        holder.kname.setText(spec.konkaniName)
        holder.catchLocation.setText(if (!item.catchLocation.isNullOrEmpty()) item.catchLocation else "N/A")

    }

}