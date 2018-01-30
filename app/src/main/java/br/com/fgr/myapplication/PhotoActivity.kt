package br.com.fgr.myapplication

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class PhotoActivity : AppCompatActivity(), SurfaceHolder.Callback {

  private var jpegCallback: PictureCallback? = null
  private var camera: Camera? = null
  private lateinit var surfaceView: SurfaceView
  private lateinit var btnPhoto: Button
  private var surfaceHolder: SurfaceHolder? = null


  override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    refreshCamera()
  }

  override fun surfaceDestroyed(p0: SurfaceHolder?) {
    camera?.stopPreview()
    camera?.release()
    camera = null
  }

  override fun surfaceCreated(p0: SurfaceHolder?) {
    try {
      // open the camera
      camera = Camera.open()
      camera?.setDisplayOrientation(180)
    } catch (e: RuntimeException) {
      // check for exceptions
      Log.e("SurfaceCreated", "Error", e)
      return
    }

    val param: Camera.Parameters? = camera?.parameters

    // modify parameter
    val sizes = param?.supportedPreviewSizes
    val selected = sizes?.get(0)
    param?.setPreviewSize(selected?.width ?: 0, selected?.height ?: 0)
    camera?.parameters = param
    try {
      // The Surface has been created, now tell the camera where to draw
      // the preview.
      camera?.setDisplayOrientation(0)
      camera?.setPreviewDisplay(surfaceHolder)
      camera?.startPreview()
    } catch (e: Exception) {
      // check for exceptions
      Log.e("SurfaceCreated", "Error", e)
      return
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo)

    surfaceView = findViewById(R.id.surfaceView)
    btnPhoto = findViewById(R.id.btn_photo)
    surfaceHolder = surfaceView.holder

    btnPhoto.setOnClickListener { _ -> camera?.takePicture(null, null, jpegCallback) }

    // Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destroyed.
    surfaceHolder?.addCallback(this)

    // deprecated setting, but required on Android versions prior to 3.0
    surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

    jpegCallback = PictureCallback { data, _ ->
      var outStream: FileOutputStream? = null
      try {
        outStream = FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()))
        outStream.write(data)
        outStream.close()
        Log.d("Log", "onPictureTaken - wrote bytes: " + data.size)
      } catch (e: Exception) {
        when (e) {
          is FileNotFoundException, is IOException -> Log.e("PhotoActivity", "Error", e)
          else -> throw e
        }
      } finally {
      }
      Toast.makeText(applicationContext, "Picture Saved", Toast.LENGTH_LONG).show()
      refreshCamera()
    }
  }

  private fun refreshCamera() {
    if (surfaceHolder?.surface == null) {
      // preview surface does not exist
      return
    }
    // stop preview before making changes
    try {
      camera?.stopPreview()
    } catch (e: Exception) {
      Log.e("RefreshCamera", "Error", e)
      // ignore: tried to stop a non-existent preview
    }

    // set preview size and make any resize, rotate or
    // reformatting changes here
    // start preview with new settings
    try {
      camera?.setPreviewDisplay(surfaceHolder)
      camera?.startPreview()
    } catch (e: Exception) {
      Log.e("RefreshCamera", "Error", e)
    }
  }
}
