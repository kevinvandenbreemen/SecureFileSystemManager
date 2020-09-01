package com.vandenbreemen.securefilesystemmanager.data

import com.vandenbreemen.mobilesecurestorage.file.ChunkedFile
import java.io.File

/**
 *
 */
class DefaultSecureFileSystemRepository: SecureFileSystemRepository {

    override fun listSecureFileSystemsIn(path: String): List<String> {
        val dir = File(path)
        if(!dir.isDirectory) {
            throw RuntimeException("Cannot do this on a file")
        }

        return dir.listFiles()?.filter { ChunkedFile.isChunkedFile(it) }?.map { it.absolutePath } ?: emptyList()
    }
}