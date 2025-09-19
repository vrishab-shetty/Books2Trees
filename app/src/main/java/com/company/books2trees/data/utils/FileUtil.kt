package com.company.books2trees.data.utils

import android.content.Context
import android.graphics.Bitmap
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

class FileUtil(private val context: Context) {

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
        uri: Uri?, selection: String?, selectionArgs: Array<String?>?
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
    fun getFileName(uri: Uri): String? {

        var name: String? = null

        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            name = cursor.getString(nameIndex)

        }

        return name
    }

    @Throws(IOException::class)
    fun getFileSize(uri: Uri): String? {
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
    fun deleteFile(uri: Uri): Boolean {
        return if ("file" == uri.scheme) {
            uri.toFile().delete()
        } else {
            try {
                context.contentResolver.delete(uri, null, null) > 0
            } catch (e: SecurityException) {
                // Log the error, you don't have permission
                false
            }
        }
    }

    @Throws(IOException::class)
    suspend fun saveBitmapFileIntoExternalStorageWithTitle(
        bitmap: Bitmap, title: String, directory: String
    ): Uri = withContext(Dispatchers.IO) {
        val file = makeEmptyFileIntoExternalStorageWithTitle(
            "$title.png", directory
        )
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        Uri.fromFile(file)
    }


}