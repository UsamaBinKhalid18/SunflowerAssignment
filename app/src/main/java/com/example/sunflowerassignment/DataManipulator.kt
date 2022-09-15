package com.example.sunflowerassignment


import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Type
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Executors
import kotlin.io.path.Path

private const val TAG = "DataManipulator"

object DataManipulator {

    private const val FILE_PATH = "/data.json"
    private val threadPool = Executors.newFixedThreadPool(4)

    fun loadFlowers(context: Context) {
        val dataString = getDataString(context)

        val listType: Type = object : TypeToken<ArrayList<Flower?>?>() {}.type
        MainActivity.flowersList.clear()
        MainActivity.flowersList.addAll(Gson().fromJson(dataString, listType))
        downloadImages(context)
    }

    private fun getDataString(context: Context): String {


        val dataFile = File(context.filesDir.path + FILE_PATH)
        return if (dataFile.exists()) {
            FileInputStream(dataFile).reader().readText()
            //context.assets.open("flowers.json").reader().readText()
        } else {
            context.assets.open("flowers.json").reader().readText()
        }
    }


    private fun downloadImages(context: Context) {
        for (flower in MainActivity.flowersList) {


            var filedoesnotexist=true
            try{
                val inputStream: InputStream? = context.contentResolver.openInputStream(Uri.parse(flower.imageUriPath))
                inputStream?.close()
                filedoesnotexist=false
            }catch (e:Exception){

            }
            if (flower.imageUriPath == null||filedoesnotexist) {
                threadPool.execute {
                    try {
                        val inputStream = URL(flower.imageUrl).openStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val filename = "${flower.name}.jpg"
                        var fos: OutputStream? = null
                        context.contentResolver?.also { resolver ->
                            val contentValues = ContentValues().apply {
                                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                                put(
                                    MediaStore.MediaColumns.RELATIVE_PATH,
                                    Environment.DIRECTORY_PICTURES
                                )
                            }
                            val imageUri: Uri? = resolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                contentValues
                            )
                            fos = imageUri?.let { resolver.openOutputStream(it) }

                            flower.imageUriPath = imageUri?.toString()

                        }
                        fos?.use {
                            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }
        threadPool.shutdown()
    }


    // collapsible list /expandable
    //nested recyclerview section and categories
    //
}