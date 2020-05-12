package com.team18.tourister.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team18.tourister.API.PLACE_NAME
import com.team18.tourister.API.PLACE_TYPE
import com.team18.tourister.R
import com.team18.tourister.models.CityPlace
import com.team18.tourister.models.SearchListModel
import kotlinx.android.synthetic.main.item.view.*

class PlaceAdapter (private val context: Context) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private var placeList  = mutableListOf<SearchListModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount() = placeList.size

    fun setupList(list: List<SearchListModel>) {
        placeList = list as MutableList<SearchListModel>
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(placeList[position].image_url).into(holder.placeImage)
        holder.placeName.text = placeList[position].name
        holder.placeName.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_detailFragmet,
                bundleOf(PLACE_NAME to placeList[position].name,
                    PLACE_TYPE to placeList[position].type),
                null,
                null
            )
        }
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeImage: ImageView
        get() = itemView.placeImg

        val placeName: TextView
        get() = itemView.placeName
    }

}