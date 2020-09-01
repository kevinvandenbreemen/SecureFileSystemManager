package com.vandenbreemen.securefilesystemmanager

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.vandenbreemen.securefilesystemmanager.data.DefaultSecureFileSystemRepository
import com.vandenbreemen.securefilesystemmanager.data.SecureFileSystemManagementInteractor

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        const val PERMISSION_REQUEST = 42
    }

    private lateinit var interactor: SecureFileSystemManagementInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        interactor = SecureFileSystemManagementInteractor(DefaultSecureFileSystemRepository(), applicationContext)
    }

    override fun onResume() {
        super.onResume()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(!Environment.isExternalStorageManager()) {
                AlertDialog.Builder(this).setTitle("Manage Files on Your Device").setMessage("This app needs to be able to read files from your device to function.  If you are okay with this please grant the requisite permission")
                    .setPositiveButton("OK") { dlg, _->
                        val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        intent.data = Uri.parse("package:${applicationContext.packageName}")
                        startActivity(intent)
                        dlg.dismiss()
                    }
                    .setNegativeButton("Cancel") {dlg,_ -> dlg.dismiss()}
                    .show()
            } else {
                refreshSFSList()
            }
        } else {

            if (checkSelfPermission(READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder(this)
                        .setTitle("Required Permissions")
                        .setMessage("This app requires file system read permissions in order to function")
                        .setPositiveButton("OK") { _, _ ->
                            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)
                        }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                        .show()
                } else {
                    requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)
                }
            } else {
                refreshSFSList()
            }
        }
    }

    private fun refreshSFSList() {
        Toast.makeText(this,
            "FILES LIST:  " + interactor.getAllSecureFileSystemFiles(), LENGTH_LONG
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST && PERMISSION_GRANTED == grantResults[0]) {
            refreshSFSList()
        } else {
            Toast.makeText(this, "Was not granted permission.  Shame", LENGTH_SHORT).show()
        }
    }
}