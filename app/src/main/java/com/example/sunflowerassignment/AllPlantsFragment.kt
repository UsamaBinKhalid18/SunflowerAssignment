package com.example.sunflowerassignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson


class AllPlantsFragment : Fragment(),ItemSelectListener {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_plants, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView= view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter=Adapter( MainActivity.flowersList,this)

        view.findViewById<FloatingActionButton>(R.id.bt_insert_plant).setOnClickListener {
            startActivity(Intent(context,InsertPlantActivity::class.java))
        }

    }

    override fun onClick(flower: Flower){
        startActivity(
            Intent(context,FlowerDetailsActivity::class.java).putExtra("flowerString",
                Gson().toJson(flower)))

    }
}