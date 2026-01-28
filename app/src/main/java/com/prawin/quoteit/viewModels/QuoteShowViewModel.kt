package com.prawin.quoteit.viewModels

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prawin.quoteit.utils.ContextHelper
import dev.shreyaspatil.capturable.controller.CaptureController
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

private const val TAG = "QuoteShowViewModel"

class QuoteShowViewModel(val contextHelper: ContextHelper) : ViewModel() {

    @OptIn(ExperimentalComposeApi::class)
    fun convertAndShareAsImage(captureController: CaptureController){
        viewModelScope.launch {
            val bitmapAsync = captureController.captureAsync()
            try {
                val bitmap: ImageBitmap = bitmapAsync.await()
                val capturedBitmap: Bitmap = bitmap.asAndroidBitmap()
                shareBitmap(capturedBitmap)
            } catch (e: CancellationException) {
                //Log.e(TAG, "failed to convert and share as Image", e)
            }
        }
    }

    fun saveBitmapToFile( bitmap: Bitmap): Uri? {
        return try {
            val imagesFolder = File(contextHelper.getContext().cacheDir, "images")
            imagesFolder.mkdirs()

            val file = File(imagesFolder, "shared_image.jpeg")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            //Log.d("Share", "File exists: ${file.exists()}")

            FileProvider.getUriForFile(contextHelper.getContext(), "${contextHelper.getContext().packageName}.fileprovider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }
    }
    @SuppressLint("QueryPermissionsNeeded")
    fun shareBitmap(bitmap: Bitmap) {
        try {
            val uri = saveBitmapToFile(bitmap) ?: return
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*" // <-- make it generic
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            val chooser = Intent.createChooser(shareIntent, "Share image via")
            contextHelper.getContext().startActivity(chooser)
        } catch (e: Exception){
            throw Exception(e)
        }
    }
}