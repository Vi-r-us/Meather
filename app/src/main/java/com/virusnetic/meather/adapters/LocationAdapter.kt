package com.virusnetic.meather.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.virusnetic.meather.R
import com.virusnetic.meather.models.LocationEntity

class LocationAdapter(val context: Context, private val locations: List<LocationEntity>) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvCityState: TextView = itemView.findViewById(R.id.tv_cityState)
        var tvFullAddress: TextView = itemView.findViewById(R.id.tv_fullAddress)
    }

    fun getItem(position: Int): LocationEntity {
        return locations[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).
            inflate(R.layout.location_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.tvCityState.text = "${location.city}, ${location.state}"
        holder.tvFullAddress.text = location.address
    }

    override fun getItemCount(): Int {
        return locations.size
    }

}