package com.vandenbreemen.securefilesystemmanager.data

/**
 * Standard repository for getting secure file systems
 */
interface SecureFileSystemRepository {

    fun listSecureFileSystemsIn(path: String): List<String>

}