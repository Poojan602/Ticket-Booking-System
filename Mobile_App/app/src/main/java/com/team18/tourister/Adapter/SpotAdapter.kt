package com.team18.tourister.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team18.tourister.R
import com.team18.tourister.models.CityPlace
import com.team18.tourister.models.SpotPlace
import kotlinx.android.synthetic.main.spot_item.view.*

class SpotAdapter(private var context: Context) : RecyclerView.Adapter<SpotAdapter.ViewHolder> () {

    private var spotList = mutableListOf<SpotPlace>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotAdapter.ViewHolder {
        return SpotAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.spot_item, parent, false)
        )
    }

    override fun getItemCount()= spotList.size

    fun setUpList(list: List<SpotPlace>) {
        spotList = list as MutableList<SpotPlace>
    }

    override fun onBindViewHolder(holder: SpotAdapter.ViewHolder, position: Int) {
        Glide.with(context).load(spotList[position].T_Image).into(holder.spotImage)
        holder.spotName.text = spotList[position].T_name
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val spotImage: ImageView
        get() = itemView.spotImage

        val spotName: TextView
        get() = itemView.spotName
    }

}