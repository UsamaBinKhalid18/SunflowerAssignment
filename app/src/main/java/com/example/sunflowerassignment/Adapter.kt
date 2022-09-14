package com.example.sunflowerassignment

import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.DrawableCompat.setTint
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class Adapter(private val dataset:MutableList<Flower>, private val itemSelectListener: ItemSelectListener):
    RecyclerView.Adapter<Adapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView=view.findViewById<ImageView>(R.id.card_image_view)
        val plantNameText=view.findViewById<TextView>(R.id.card_plant_name)
        val cardView=view.findViewById<CardView>(R.id.card_layout)
        val favButton=view.findViewById<ImageButton>(R.id.card_favorite_bt)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)
        return ViewHolder(itemLayout)
    }


    override fun getItemCount(): Int=dataset.count()


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =dataset[position]
        holder.plantNameText.text=item.name
        if(item.isFavorite)
            holder.favButton.setImageResource(R.drawable.heart_fill)
        holder.favButton.setOnClickListener{
            item.isFavorite=!item.isFavorite
            FavoriteFragment.updateFavourites()
            if(item.isFavorite)
                holder.favButton.setImageResource(R.drawable.heart_fill)
            else
                holder.favButton.setImageResource(R.drawable.heart)
        }
        holder.cardView.setOnClickListener {
            itemSelectListener.onClick(item)
        }

        if(item.imageUriPath!=null)
            holder.imageView.setImageURI(Uri.parse(item.imageUriPath))

    }
}