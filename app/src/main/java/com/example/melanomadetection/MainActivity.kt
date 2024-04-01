package com.example.melanomadetection

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.example.melanomadetection.databinding.MainActivityBinding
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import org.slf4j.LoggerFactory

class MainActivity : ComponentActivity() {

    companion object {
        private val logger = LoggerFactory.getLogger("com.example.melanomadetection")
    }


    private lateinit var binding: MainActivityBinding
    private val CAMERA_PERMISSION_REQUEST_CODE: Int = 100
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.cameraButton.setOnClickListener { requestCameraPermission() }

    }

    private fun requestCameraPermission() {

        logger.debug("Accessing: ${Thread.currentThread().stackTrace[2].methodName} function")
        val cameraPermission: Int = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (cameraPermission != PackageManager.PERMISSION_GRANTED)
        {
            logger.debug("Camera permission not granted. Asking for it now...")
            // a callback function 'onRequestPermissionsResults' gets called to handle the result
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )

        }
        else {
            openCamera()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted, open the camera
                openCamera()
            } else {
                // permission denied, show a message or handle the situation accordingly
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {

        logger.debug("Greetings")

        val cameraIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivity(cameraIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }

    }

}