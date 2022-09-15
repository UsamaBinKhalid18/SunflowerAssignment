package com.example.sunflowerassignment

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.google.gson.Gson
import org.w3c.dom.Text

class FlowerDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flower_details)
        val string=intent.extras?.getString("flowerString")

        val flower=Gson().fromJson(string,Flower::class.java)
        setSupportActionBar( findViewById(R.id.toolbar_secondary))
        findViewById<ImageView>(R.id.detail_image_view).setImageURI(Uri.parse(flower.imageUriPath))
        findViewById<TextView>(R.id.detail_plantid).text="Plant Id : ${flower.plantId}"
        findViewById<TextView>(R.id.detail_watering).text="Water every ${flower.wateringInterval} "
        findViewById<TextView>(R.id.detail_zoneNumber).text="Zone Number : ${flower.growZoneNumber}"
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_secondary).title=flower.name
        findViewById<TextView>(R.id.detail_description).text=flower.description
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}