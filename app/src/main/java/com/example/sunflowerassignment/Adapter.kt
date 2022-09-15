package com.example.sunflowerassignment

import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "Adapter"

class Adapter(private val dataset:MutableList<Flower>, private val itemSelectListener: ItemSelectListener):
    RecyclerView.Adapter<Adapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView :ImageView=view.findViewById(R.id.card_image_view)
        val plantNameText:TextView=view.findViewById(R.id.card_plant_name)
        val cardView:CardView=view.findViewById(R.id.card_layout)
        val favButton:ImageButton=view.findViewById(R.id.card_favorite_bt)

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

            if(item.isFavorite){
                holder.favButton.setImageResource(R.drawable.heart_fill)
                FavoriteFragment.addFavouriteToRecycleView()
            }
            else{
                holder.favButton.setImageResource(R.drawable.heart)
                FavoriteFragment.removeFavouriteFromRecycleView()
            }
        }
        holder.cardView.setOnClickListener {
            itemSelectListener.onClick(item)
        }

        if(item.imageUriPath!=null)
            holder.imageView.setImageURI(Uri.parse(item.imageUriPath))

    }
}