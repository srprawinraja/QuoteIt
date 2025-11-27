package com.example.quoteit.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Button
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteit.R
import com.example.quoteit.utils.ContextHelper
import dev.shreyaspatil.capturable.controller.CaptureController
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


class QuoteShowViewModel(val contextHelper: ContextHelper) : ViewModel() {

    @OptIn(ExperimentalComposeApi::class)
    fun convertAndShareAsImage(captureController: CaptureController){
        viewModelScope.launch {
            val bitmapAsync = captureController.captureAsync()
            try {
                val bitmap: ImageBitmap = bitmapAsync.await()
                val capturedBitmap: Bitmap = bitmap.asAndroidBitmap()
                shareBitmap(capturedBitmap)
            } catch (error: CancellationException) {
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
            Log.d("Share", "File exists: ${file.exists()}")

            FileProvider.getUriForFile(contextHelper.getContext(), "${contextHelper.getContext().packageName}.fileprovider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    @SuppressLint("QueryPermissionsNeeded")
    fun shareBitmap(bitmap: Bitmap) {
        val uri = saveBitmapToFile( bitmap) ?: return
        Log.i("uri", "uri is printed $uri")
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*" // <-- make it generic
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(shareIntent, "Share image via")
        val resolveInfos = contextHelper.getContext().packageManager.queryIntentActivities(shareIntent, 0)
        for (info in resolveInfos) {
            Log.d("Share", "Can handle: ${info.activityInfo.packageName}")
        }
        contextHelper.getContext().startActivity(chooser)
    }
}