package com.vandenbreemen.securefilesystemmanager.data

import android.content.Context
import java.io.File

class SecureFileSystemManagementInteractor(private val secureFileSystemRepository: SecureFileSystemRepository, private val context: Context) {

    private val supportedPaths: List<File> by lazy {
        val contextPath = context.getExternalFilesDir(null)

        val externalPath = contextPath!!.absolutePath.replace(Regex("Android[/]data[/]${context.packageName}.*"), "")

        listOf(File(externalPath))
    }

    fun getAllSecureFileSystemFiles(): List<String> {

        val result = mutableListOf<String>()
        supportedPaths.filterNotNull().forEach { path->
            addSecureFileSystems(path, result)
        }

        return result
    }

    private fun addSecureFileSystems(inDirectory: File, toList: MutableList<String>) {
        toList.addAll(secureFileSystemRepository.listSecureFileSystemsIn(
            inDirectory.absolutePath
        ))

        inDirectory.listFiles()?.filter { f->f.isDirectory }?.forEach { addSecureFileSystems(it, toList) }
    }

}