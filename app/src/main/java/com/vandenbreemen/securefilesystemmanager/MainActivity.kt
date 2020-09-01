package com.vandenbreemen.securefilesystemmanager

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.vandenbreemen.securefilesystemmanager.data.DefaultSecureFileSystemRepository

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        const val PERMISSION_REQUEST = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        if(checkSelfPermission(READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)){
                AlertDialog.Builder(this)
                    .setTitle("Required Permissions")
                    .setMessage("This app requires file system read permissions in order to function")
                    .setPositiveButton("OK") { _, _ ->
                        requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)
                    }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()}
                    .show()
            } else {
                requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)
            }
        } else {
            refreshSFSList()
        }
    }

    private fun refreshSFSList() {
        Toast.makeText(this,
            "DOWNLOADS DIR LIST:  " + DefaultSecureFileSystemRepository().listSecureFileSystemsIn(
                applicationContext.getExternalFilesDir(
                    Environment.DIRECTORY_DOWNLOADS
                )!!.absolutePath
            ), LENGTH_LONG
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