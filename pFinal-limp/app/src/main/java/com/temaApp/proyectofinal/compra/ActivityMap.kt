package com.temaApp.proyectofinal.compra

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.temaApp.proyectofinal.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

class ActivityMap : AppCompatActivity(), OnMapReadyCallback,OnMapClickListener {
    private lateinit var btnvolver: ImageView
    private lateinit var google: GoogleMap
    private lateinit var locationManager: LocationManager

    companion object{
        const val REQUEST_CODE_LOCATION = 2803
    }


    private var markado: Marker?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicompontents()
        mapa()
    }

    private fun mapa() {
        var mapFrament = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrament.getMapAsync(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun inicompontents() {
        btnvolver = findViewById(R.id.btnMapVolver)
        btnvolver.setOnClickListener {
            val intent = Intent(this, CheckOut::class.java)
            startActivity(intent)
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.google = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
        googleMap.setOnMapClickListener(this)
        googleMap.setOnMarkerClickListener {marker ->
            if(marker == this.markado){
                marker.remove()
                return@setOnMarkerClickListener true
            }
            false

        }
        val ubicacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (ubicacion != null) {
            val latilongi = LatLng(ubicacion.latitude, ubicacion.longitude)
            google.moveCamera(CameraUpdateFactory.newLatLngZoom(latilongi, 15f))
        }else {
            Log.d("Ubicación", "No se pudo obtener la ubicación")
        }

        habilitarubicacion()

    }

    private fun habilitarubicacion() {
        if(!::google.isInitialized)return
        if (isLocationPermissionGranted()){
            google.isMyLocationEnabled = true
        }else{
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this, "Revisar los permisos", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ActivityMap.REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            ActivityMap.REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                google.isMyLocationEnabled = true
            }else{
                Toast.makeText(
                    this,
                    "Para Activar la localizacion ve a ajustes y acepta los Permisos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    override fun onMapClick(lati: LatLng) {
        markado?.remove()
        markado = google.addMarker(MarkerOptions()
            .position(lati)
            .title("Punto de Entrega"))

        mostrarMenssaje(lati)
    }

    private fun mostrarMenssaje(lati: LatLng) {


        CoroutineScope(Dispatchers.IO).launch {
            val geocoder = Geocoder(this@ActivityMap, Locale.getDefault())
            try {
                val addresses: MutableList<Address>? = geocoder.getFromLocation(lati.latitude, lati.longitude, 1)
                if (addresses != null) {
                    if (addresses.isNotEmpty()) {
                        val address: Address = addresses[0]
                        val addressText: String = address.getAddressLine(0)

                        runOnUiThread {
                            val intent = Intent(this@ActivityMap, CheckOut::class.java)
                            //Toast.makeText(this@ActivityMap, "Ubicación guardada correctamente: $addressText", Toast.LENGTH_LONG).show()
                            intent.putExtra("direccion", addressText)
                            startActivity(intent)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@ActivityMap, "No se pudo obtener la dirección.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@ActivityMap, "Error al obtener la dirección.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}