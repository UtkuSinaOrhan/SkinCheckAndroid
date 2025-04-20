package com.example.newskincheckapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

class MainActivity : AppCompatActivity() {

    // UI components
    private lateinit var btnChoosePhoto: Button
    private lateinit var btnTakePhoto: Button
    private lateinit var btnAnalyze: Button
    private lateinit var imageView: ImageView
    private lateinit var textAnalyzing: TextView

    // Launchers for activity results
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    // Firebase storage reference
    private val storageReference = FirebaseStorage.getInstance().reference

    // Stores the URI of the selected image
    private var selectedImageUri: Uri? = null

    // Cache to store analysis results for images
    private val analysisCache = mutableMapOf<String, Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        initializeViews()

        // Set up activity result launchers
        setupLaunchers()

        // Set up listeners for button clicks
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        textAnalyzing.text = "" // Clear the analyzing text when resuming
    }

    // Initialize UI components
    private fun initializeViews() {
        btnChoosePhoto = findViewById(R.id.btnChoosePhoto)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        btnAnalyze = findViewById(R.id.btnAnalyze)
        imageView = findViewById(R.id.imageView)
        textAnalyzing = findViewById(R.id.textAnalyzing)
        textAnalyzing.text = ""
    }

    // Set up activity result launchers for picking and capturing images
    private fun setupLaunchers() {
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                selectedImageUri = result.data!!.data
                selectedImageUri?.let {
                    imageView.setImageURI(it)
                    textAnalyzing.text = ""
                }
            } else {
                Toast.makeText(this, "No photo selected!", Toast.LENGTH_SHORT).show()
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val photo: Bitmap? = result.data!!.extras?.get("data") as? Bitmap
                photo?.let {
                    imageView.setImageBitmap(it)
                    textAnalyzing.text = ""
                    saveBitmapToFirebase(it)
                }
            } else {
                Toast.makeText(this, "No photo captured!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Set up listeners for button clicks
    private fun setupListeners() {
        btnChoosePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        btnTakePhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)
        }

        btnAnalyze.setOnClickListener {
            if (selectedImageUri != null) {
                val uriString = selectedImageUri.toString()
                if (analysisCache.containsKey(uriString)) {
                    // Use cached analysis result if available
                    val cachedResult = analysisCache[uriString]!!
                    startAnalysis(uriString, cachedResult)
                } else {
                    // Upload photo to Firebase for analysis
                    uploadPhotoToFirebase(selectedImageUri!!)
                }
            } else {
                Toast.makeText(this, "Please select or take a photo first!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Upload selected photo to Firebase
    private fun uploadPhotoToFirebase(uri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val photoRef = storageReference.child("users/$userId/photos/${System.currentTimeMillis()}.jpg")

            photoRef.putFile(uri)
                .addOnSuccessListener {
                    photoRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        generateAndCacheAnalysis(uri.toString(), downloadUri.toString())
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload photo: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User is not authenticated!", Toast.LENGTH_SHORT).show()
        }
    }

    // Save bitmap (captured photo) to Firebase
    private fun saveBitmapToFirebase(bitmap: Bitmap) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val photoRef = storageReference.child("users/$userId/photos/${System.currentTimeMillis()}.jpg")
            val byteArrayOutputStream = java.io.ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()

            photoRef.putBytes(data)
                .addOnSuccessListener {
                    photoRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val uriString = downloadUri.toString()
                        if (!analysisCache.containsKey(uriString)) {
                            generateAndCacheAnalysis(uriString, uriString)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.failed_to_upload_photo), Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please sign in first!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    // Generate a random analysis result and cache it
    private fun generateAndCacheAnalysis(uriString: String, imageUrl: String) {
        val randomResult = generateRandomAnalysisResult()
        analysisCache[uriString] = randomResult
        startAnalysis(imageUrl, randomResult)
    }

    // Start analysis and navigate to ResultActivity with the results
    private fun startAnalysis(imageUrl: String, result: Pair<String, String>) {
        textAnalyzing.text = "Analyzing..."
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("RESULT_TITLE", result.first)
                putExtra("RESULT_DETAILS", result.second)
                putExtra("IMAGE_URL", imageUrl)
            }
            startActivity(intent)
            resetAnalysisState()
        }, 3000)
    }

    // Generate a random analysis result (placeholder function)
    private fun generateRandomAnalysisResult(): Pair<String, String> {
        return AnalysisData.results.random()
    }

    // Reset analysis state after completion
    private fun resetAnalysisState() {
        textAnalyzing.text = ""
        selectedImageUri = null
        imageView.setImageDrawable(null)
    }
}
