package com.vandenbreemen.securefilesystemmanager.data

import android.content.Context
import android.os.Environment

class SecureFileSystemManagementInteractor(private val secureFileSystemRepository: SecureFileSystemRepository, private val context: Context) {

    private val supportedPaths = listOf(
        context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS
        ),
        context.getExternalFilesDir(
            Environment.DIRECTORY_DOWNLOADS
        )
    )

    fun getAllSecureFileSystemFiles(): List<String> {

        val result = mutableListOf<String>()
        supportedPaths.filterNotNull().forEach { path->
            result.addAll(secureFileSystemRepository.listSecureFileSystemsIn(
                path.absolutePath
            ))
        }

        return result
    }

}