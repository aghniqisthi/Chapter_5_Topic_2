package com.example.chapter5topic2

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.SmsManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //normal permission munculin gambar pake glide
        btnImgPerm.setOnClickListener {
            Glide.with(this).load("https://wallpaperaccess.com/full/2454114.jpg").centerCrop().into(imgGambar)
        }

        //normal permission internet ke youtube
        btnInternetPerm.setOnClickListener{
            val pindah = Intent(Intent.ACTION_VIEW)
            pindah.data = Uri.parse("https://www.youtube.com")
            startActivity(pindah)
        }

        //dangerous permission location
        btnLocPerm.setOnClickListener {
            //cek permission di manifest
            val cekPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

            //kalo permission di packagemanager diizinkan, bisa lanjut
            if (cekPerm == PackageManager.PERMISSION_GRANTED) {
                toast("Location Diizinkan")
                getLongLat()
            }
            //kalo belom diizinkan, request permission location ke user
            else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 201)
                toast("Location Ditolak")
            }
        }

        //dangerous permission camera
        btnDangerPerm.setOnClickListener {
            //intent ke image capture
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                //request permission ke camera, request code success 201
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 201)
                startActivityForResult(cameraIntent, 1)
            } catch (exception: ActivityNotFoundException) {
                toast("Error!")
            }
        }
    }

    fun toast(pesan: String) {
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
    }

    //failed soalnya nilainya null terus
    @SuppressLint("MissingPermission")
    fun getLongLat() {
        val locManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //ini buat dapetin last lokasi devicenya dimana
        val location: Location?
        location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        //trus toast hasilnya tapi null terus gapaham
        toast("Latitude: ${location?.latitude} ; Longitude : ${location?.longitude}")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            201 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {
                    toast("Permission Location Diizinkan")
                    getLongLat()
                }
                else toast("Permission Location Ditolak")
            }
            else -> toast("Request doesn't match!")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            imgGambar.setImageBitmap(bitmap)
        } else {
            // some error to be shown here
        }
    }
}
