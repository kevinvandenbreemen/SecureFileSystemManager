package com.vandenbreemen.securefilesystemmanager

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.CancellationSignal
import android.util.Log
import com.vandenbreemen.securefilesystemmanager.data.DefaultSecureFileSystemRepository
import com.vandenbreemen.securefilesystemmanager.data.SecureFileSystemManagementInteractor

public class SecureFileSystemsProvider : ContentProvider() {

    companion object {
        val AUTHORITY = SecureFileSystemsProvider::class.java.canonicalName
        val SECURE_FILE_SYS_LIST_PATH = "lsSFS"
        val SECURE_FILE_SYS_LIST_CODE = 1
    }

    private val secureFileSystemInteractor : SecureFileSystemManagementInteractor by lazy {
        context?.let {
            return@lazy  SecureFileSystemManagementInteractor(DefaultSecureFileSystemRepository(), it)
        } ?: run {
            throw RuntimeException("Context not available!")
        }

    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    init {
        uriMatcher.addURI(AUTHORITY, SECURE_FILE_SYS_LIST_PATH, SECURE_FILE_SYS_LIST_CODE)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return -1
    }

    override fun getType(uri: Uri): String? {

        Log.i("SFSContentProvider", "get mimetype for $uri")

        if(uriMatcher.match(uri) == SECURE_FILE_SYS_LIST_CODE) {
            return "text/plain"
        }

        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
        cancellationSignal: CancellationSignal?
    ): Cursor? {
        Log.i("SFSContentProvider", "query using uri=$uri")

        if(uriMatcher.match(uri) == SECURE_FILE_SYS_LIST_CODE) {
            val allFiles = secureFileSystemInteractor.getAllSecureFileSystemFiles()

            val cursor = MatrixCursor(
                arrayOf("path")
            )
            allFiles.forEach { path->
                cursor.newRow().add("path", path)
            }

            context?.apply {
                cursor.setNotificationUri(contentResolver, uri)
            }

            return cursor
        }

        return null
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        return query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal = null)
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
