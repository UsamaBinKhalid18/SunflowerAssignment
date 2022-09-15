package com.example.sunflowerassignment

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.sunflowerassignment.DataManipulator.loadFlowers
import com.example.sunflowerassignment.MainActivity.Companion.flowersList
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.net.URL
import java.util.concurrent.Executors
import com.google.gson.Gson
import java.io.FileOutputStream
import javax.sql.DataSource

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(){
    private val FILE_PATH="/data.json"
    companion object{
        val flowersList= mutableListOf<Flower>()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        loadFlowers(this)

        val viewPager=findViewById<ViewPager2>(R.id.main_viewPager)
        val tabLayout=findViewById<TabLayout>(R.id.main_tab_layout)

        viewPager.adapter=TabsAdapter(this)

        TabLayoutMediator(tabLayout,viewPager){tab,position->
            tab.text=when(position){
                0->"All Plants"
                else->"Favourite Plants"
            }

        }.attach()

        
    }




    override fun onSaveInstanceState(outState: Bundle) {

        Log.d(TAG, "onSaveInstanceState: called")
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        try{

            val outStream= FileOutputStream(File(filesDir.path+FILE_PATH))
            outStream.write(Gson().toJson(flowersList).toByteArray())
            outStream.close()

        }catch (e:Exception){
            e.printStackTrace()
        }
        Log.d(TAG, "onPause: ")
        super.onPause()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }


}