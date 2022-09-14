package com.example.sunflowerassignment

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class InsertPlantActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks {


    private lateinit var uri: Uri
    val imageResultActivity=registerForActivityResult(ActivityResultContracts.TakePicture()){
        findViewById<ImageView>(R.id.image_view_insert_plant).setImageURI(uri)
        findViewById<Button>(R.id.bt_new_photo).visibility= View.INVISIBLE
        findViewById<Button>(R.id.bt_photo_from_gallery).visibility= View.INVISIBLE

    }
    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { findViewById<ImageView>(R.id.image_view_insert_plant).setImageURI(uri) }
        findViewById<Button>(R.id.bt_new_photo).visibility= View.INVISIBLE
        findViewById<Button>(R.id.bt_photo_from_gallery).visibility= View.INVISIBLE

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_plant)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<Button>(R.id.bt_new_photo).setOnClickListener {
            imageResultActivity.launch(uri)
        }
        findViewById<Button>(R.id.bt_photo_from_gallery).setOnClickListener {
            selectImageFromGalleryResult.launch("*/*")

        }

        if(EasyPermissions.hasPermissions(this,android.Manifest.permission.CAMERA)){

        }else{
            EasyPermissions.requestPermissions(this,"Need Camera Access",101, Manifest.permission.CAMERA)

        }

        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        uri= FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)

        findViewById<Button>(R.id.bt_add_new_plant).setOnClickListener{
            val plantId=findViewById<EditText>(R.id.et_plantId).text.toString()
            val plantName=findViewById<EditText>(R.id.et_plantname).text.toString()
            val plantDesc=findViewById<EditText>(R.id.et_plantdescription).text.toString()
            val growthZone=findViewById<EditText>(R.id.et_zonenumber).text.toString().toInt()
            val wateringInterval=findViewById<EditText>(R.id.et_wateringdetail).text.toString().toInt()


            MainActivity.flowersList.add(Flower(plantName,plantId,plantDesc,growthZone,wateringInterval, isFavorite = false, imageUriPath = uri.toString(), imageUrl = null))
            finish()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        imageResultActivity.launch(uri)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }
}