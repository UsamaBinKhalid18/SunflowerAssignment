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

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(){
    val threadPool= Executors.newFixedThreadPool(4)
    private val FILE_PATH="/data.json"
    companion object{
        val flowersList= mutableListOf<Flower>()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        val dataString=getDataString()
        flowersList.addAll(DataSource.loadFlowers(dataString))
        downloadAllImages()

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
    private fun getDataString():String{

        var dataFile=File(filesDir.path+FILE_PATH)
        return if(dataFile.exists()){

            FileInputStream(dataFile).reader().readText()
            //assets.open("flowers.json").reader().readText()
        }else{
            assets.open("flowers.json").reader().readText()
        }

//        collapsibllist /expandable
        //nested recyclerview section and categories
        //
    }

    override fun onPause() {
        threadPool.execute { try{

            val outStream= FileOutputStream(File(filesDir.path+FILE_PATH))
            outStream.write(Gson().toJson(flowersList).toByteArray())
            outStream.close()

        }catch (e:Exception){
            e.printStackTrace()
        }

        }
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState: ")
        super.onSaveInstanceState(outState)
    }
    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }
    private fun downloadAllImages(){
        for (flower in flowersList){
            if(flower.imageUriPath==null){
                threadPool.execute {
                    try {
                        val inputStream= URL(flower.imageUrl).openStream()
                        val bitmap= BitmapFactory.decodeStream(inputStream)
                        val filename = "${flower.name}.jpg"
                        var fos: OutputStream? = null
                        this.contentResolver?.also { resolver ->
                            val contentValues = ContentValues().apply {
                                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                            }
                            val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                            fos = imageUri?.let { resolver.openOutputStream(it) }

                            flower.imageUriPath=imageUri?.toString()
                            Log.d(TAG, "onCreate: ${flower.imageUriPath}")
                        }
                        fos?.use {
                            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        }
                    }catch (e:Exception){e.printStackTrace()}
                }
            }
        }
    }


}