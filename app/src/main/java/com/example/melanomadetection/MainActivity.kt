package com.example.melanomadetection

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.example.melanomadetection.databinding.MainActivityBinding
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast

class MainActivity : ComponentActivity() {

    private lateinit var binding: MainActivityBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.cameraButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                requestCameraPermission()
            }})

    }

    private fun requestCameraPermission() {

        val cameraPermission: Int = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA)

        if (cameraPermission != PackageManager.PERMISSION_GRANTED)
        {
            // a callback function 'onRequestPermissionsResults' gets called to handle the result
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )

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

        val cameraIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }

    }

}