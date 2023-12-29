package com.company.books2trees.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.net.toFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt


object FileUtils {

    const val DIRECTORY_THUMBNAILS = "Thumbnails"

    fun getScaledBitmap(actualWidth: Int, actualHeight: Int, screenWidth: Int): Bitmap {

        val bitmap = Bitmap.createBitmap(
            screenWidth,
            (screenWidth.toFloat() / actualWidth * actualHeight).toInt(),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        return bitmap

    }


    fun isGoogleDrive(uri: Uri): Boolean {
        return uri.authority.equals("com.google.android.apps.docs.storage", ignoreCase = true)
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }


    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String?>?
    ): String? {

        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(
            column
        )

        context.contentResolver.query(
            uri!!, projection, selection, selectionArgs, null
        )?.use {
            val columnIndex = it.getColumnIndexOrThrow(column)
            return it.getString(columnIndex)
        }

        return null
    }

    private fun makeEmptyFileIntoExternalStorageWithTitle(
        context: Context,
        title: String,
        directory: String
    ): File {

        val appDir = File(context.getExternalFilesDir(null), directory)
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        return File(appDir.absolutePath, title)
    }


    @Throws(IOException::class)
    fun getFileName(context: Context, uri: Uri): String? {

        var name: String? = null

        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            name = cursor.getString(nameIndex)

        }

        return name
    }

    @Throws(IOException::class)
    suspend fun getFileSize(context: Context, uri: Uri): String? {
        var size: String? = null

        context.contentResolver.query(uri, null, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            var sizeInBytes: Double = cursor.getLong(sizeIndex).toDouble()
            var count = 0
            while (sizeInBytes >= 1000.0) {
                sizeInBytes /= 1000.0
                count++
            }
            val unit = when (count) {
                0 -> "B"
                1 -> "KB"
                2 -> "MB"
                3 -> "GB"
                else -> error("Too Big File")
            }
            size = "${(sizeInBytes * 100.0).roundToInt() / 100.0} $unit"
        }


        return size
    }

    @Throws(SecurityException::class, IOException::class)
    fun deleteFileWithUri(context: Context, uri: Uri): Boolean {
        return uri.toFile().delete()
    }

    @Throws(IOException::class)
    suspend fun saveBitmapFileIntoExternalStorageWithTitle(
        context: Context, bitmap: Bitmap, title: String, directory: String
    ): Uri = withContext(Dispatchers.IO) {
        val file = makeEmptyFileIntoExternalStorageWithTitle(
            context, "$title.png", directory
        )
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        Uri.fromFile(file)
    }


}