package com.example.sunflowerassignment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.File

class FavoriteFragment : Fragment(),ItemSelectListener {

    companion object{
        val favoriteList= mutableListOf<Flower>()
        lateinit var favouriteRecyclerView: RecyclerView
        fun addFavouriteToRecycleView(){
            for (flower in MainActivity.flowersList){
                if(flower.isFavorite && flower !in favoriteList) {
                    favoriteList.add(flower)
                    break
                }
            }
            if(this::favouriteRecyclerView.isInitialized)
                favouriteRecyclerView.adapter?.notifyItemInserted(favoriteList.size-1)
        }
        fun removeFavouriteFromRecycleView(){

            for (flower in favoriteList){
                if(!flower.isFavorite){
                    AllPlantsFragment.updateItemInRV(MainActivity.flowersList.indexOf(flower))

                    break
                }
            }


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
         favouriteRecyclerView=view.findViewById(R.id.recycler_view)

        addFavouriteToRecycleView()
        favouriteRecyclerView.adapter=Adapter(favoriteList,this)
    }

    override fun onClick(flower: Flower) {
        if (flower.imageUriPath == null) {
            Toast.makeText(context,"Image not downloaded yet please wait",Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(
            Intent(context,FlowerDetailsActivity::class.java).putExtra("flowerString",
                Gson().toJson(flower)))
    }
}