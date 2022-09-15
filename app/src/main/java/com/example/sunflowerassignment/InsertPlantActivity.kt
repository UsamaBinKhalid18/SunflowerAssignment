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
import androidx.core.widget.addTextChangedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.regex.Pattern

class InsertPlantActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {


    private lateinit var uri: Uri
    val imageResultActivity = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        findViewById<ImageView>(R.id.image_view_insert_plant).setImageURI(uri)

    }
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { findViewById<ImageView>(R.id.image_view_insert_plant).setImageURI(uri) }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_plant)
        setSupportActionBar(findViewById(R.id.toolbar))



        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        uri = FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )

        findViewById<Button>(R.id.bt_new_photo).setOnClickListener {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA)) {
                imageResultActivity.launch(uri)
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    "Need Camera Access",
                    101,
                    Manifest.permission.CAMERA
                )
            }


        }
        findViewById<Button>(R.id.bt_photo_from_gallery).setOnClickListener {
            selectImageFromGalleryResult.launch("*/*")

        }


        findViewById<TextInputEditText>(R.id.et_wateringdetail).addTextChangedListener {

            if(it.toString().matches(Regex("\\p{Digit}+ hours")))
                findViewById<TextInputLayout>(R.id.til_wateringdetail).error=null

            else
                findViewById<TextInputLayout>(R.id.til_wateringdetail).error="Must match {number} hours"
        }





        findViewById<Button>(R.id.bt_add_new_plant).setOnClickListener {
            val plantId = findViewById<TextInputEditText>(R.id.et_plantId).text.toString()
            val plantName = findViewById<TextInputEditText>(R.id.et_plantname).text.toString()
            val plantDesc =
                findViewById<TextInputEditText>(R.id.et_plantdescription).text.toString()
            val growthZone =
                findViewById<TextInputEditText>(R.id.et_zonenumber).text.toString().toInt()
            val wateringInterval =
                findViewById<TextInputEditText>(R.id.et_wateringdetail).text.toString().toInt()


            MainActivity.flowersList.add(
                Flower(
                    plantName,
                    plantId,
                    plantDesc,
                    growthZone,
                    wateringInterval,
                    isFavorite = false,
                    imageUriPath = uri.toString(),
                    imageUrl = null
                )
            )
            finish()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        imageResultActivity.launch(uri)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }
}