package com.submission.stoup.ui.add

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.submission.stoup.data.utils.getImageUri
import com.submission.stoup.data.utils.reduceFileImage
import com.submission.stoup.data.utils.uriToFile
import com.submission.stoup.databinding.ActivityAddStoryBinding
import com.submission.stoup.ui.viewmodelfactory.ViewModelFactory

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val addViewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null
    private var currentLocation: Location? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Request allowed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Request denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, REQUIRED_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            requestLocationPermissionLauncher.launch(REQUIRED_LOCATION_PERMISSION)
        } else {
            getCurrentLocation()
        }

        binding.buttonSelectPhoto.setOnClickListener { startGallery() }
        binding.buttonSelectCamera.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener { addStory() }
    }

    private fun addStory() {
        val description = binding.edAddDescription.text.toString()
        val originalFile = uriToFile(currentImageUri!!, this@AddStoryActivity)
        val reducedFile = originalFile.reduceFileImage()

        val latitude = currentLocation?.latitude
        val longitude = currentLocation?.longitude

        addViewModel.addStory(description, reducedFile, latitude, longitude)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No file selected")
        }
    }

    private fun startCamera() {
        val imageUri = getImageUri(this)
        currentImageUri = imageUri
        launcherIntentCamera.launch(imageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
            Log.e("Camera", "Failed to take a pic")
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            Glide.with(this)
                .load(uri)
                .into(binding.ivSelectedPhoto)
        }
    }

    private fun getCurrentLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
                currentLocation = location
            } ?: run {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Acces for location is not granted", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
        private const val REQUIRED_LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
    }
}
