package com.example.sunflowerassignment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class FavoriteFragment : Fragment(),ItemSelectListener {

    companion object{
        val favoriteList= mutableListOf<Flower>()
        lateinit var recyclerView: RecyclerView
        fun updateFavourites(){

            favoriteList.clear()
            for (flower in MainActivity.flowersList)
                if(flower.isFavorite)
                    favoriteList.add(flower)
            if(this::recyclerView.isInitialized)
                recyclerView.adapter?.notifyDataSetChanged()


        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         recyclerView=view.findViewById<RecyclerView>(R.id.recycler_view)

        updateFavourites()
        recyclerView.adapter=Adapter(favoriteList,this)
    }

    override fun onClick(flower: Flower) {
        startActivity(
            Intent(context,FlowerDetailsActivity::class.java).putExtra("flowerString",
                Gson().toJson(flower)))
    }
}