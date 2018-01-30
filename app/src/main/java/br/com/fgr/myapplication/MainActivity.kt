package br.com.fgr.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

  private lateinit var btnTakePhoto: Button

  private lateinit var mCurrentPhotoPath: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btnTakePhoto = findViewById(R.id.btn_take_photo)
    btnTakePhoto.setOnClickListener { _ ->
      startActivity(Intent(this@MainActivity, PhotoActivity::class.java))
//      takePhoto()
    }
    findViewById<View>(R.id.btn2).setOnClickListener { _ ->
      startActivity(Intent(MainActivity@ this, Main2Activity::class.java))
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

    }
  }

  private fun takePhoto() {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePictureIntent.resolveActivity(packageManager) != null) {
      val photoFile: File? = try {
        createImageFile()
      } catch (ex: IOException) {
        Log.d("Main", "error", ex)
        null
      }
      if (photoFile != null) {
        val uriForFile = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY,
            photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
      }
    }
  }

  @Throws(IOException::class)
  private fun createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir      /* directory */
    )

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath()
    return image
  }

  companion object {
    const val REQUEST_IMAGE_CAPTURE: Int = 676
  }
}
