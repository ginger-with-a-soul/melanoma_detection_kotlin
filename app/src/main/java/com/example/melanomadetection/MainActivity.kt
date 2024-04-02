package com.example.melanomadetection

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.example.melanomadetection.databinding.MainActivityBinding
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.slf4j.LoggerFactory

object PermissionManager {

    private val REQUEST_CODE: Int = 100
    private var operation: (() -> Unit)? = null
    fun requestPermission(context: Context, permission: String, op: () -> Unit) : Unit {

        operation = op
        val permissionStatus: Int = ContextCompat.checkSelfPermission(context, permission)


        if (permissionStatus != PackageManager.PERMISSION_GRANTED)
        {
            // a callback function 'onRequestPermissionsResults' gets called to handle the result
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(permission),
                REQUEST_CODE
            )

        }
        else {
            operation?.let { it() }
        }
    }

    fun handlePermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) : String? {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted, execute the function that needed it
                operation?.let { it() }
            } else {
                // permission denied, show a message or handle the situation accordingly
                return "${permissions[0].split('.')[2]} permission denied"
            }
        }

        return null
    }
}

class MainActivity : ComponentActivity() {

    companion object {
        private val logger = LoggerFactory.getLogger("com.example.melanomadetection")
    }

    private lateinit var binding: MainActivityBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.cameraButton.setOnClickListener { PermissionManager.requestPermission(this, Manifest.permission.CAMERA, ::openCamera) }
        binding.galleryButton.setOnClickListener { PermissionManager.requestPermission(this, Manifest.permission.READ_MEDIA_IMAGES, ::openGallery) }

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val result: String? = PermissionManager.handlePermissionResult(requestCode, permissions, grantResults)
        if (result != null) {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {


    }

    private fun openCamera() {

        logger.debug("Greetings from camera!")

        val cameraIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivity(cameraIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }

    }

}