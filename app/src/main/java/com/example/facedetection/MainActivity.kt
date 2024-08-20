package com.example.facedetection

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager.OnActivityResultListener
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.facedetection.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.button.setOnClickListener{

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager) != null){
                startActivityForResult(intent,332005)
            }else{
                Toast.makeText(this, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
            }
        }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 332005 && resultCode == RESULT_OK){
            val extras = data?.extras
            val bitmap = extras?.get("data") as Bitmap
            detectFace(bitmap)
        }
    }

    private fun detectFace(bitmap: Bitmap) {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)

        val result = detector.process(image)
            .addOnSuccessListener { faces ->
               var resultText = " "
                var i  = 1
                for (face in faces){
                    resultText = "No. of Faces : $i" +
                            "\n Smile : ${face.smilingProbability}%" +
                            "\n Left Eye Open : ${face.leftEyeOpenProbability}%" +
                            "\n Right Eye Open : ${face.rightEyeOpenProbability}%"
                }
                if (faces.isEmpty()){
                    Toast.makeText(this, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
                }else{
                    binding.textView.text = resultText
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
            }
    }
}
