package ru.dublgis.dgismobile.sdktestapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import ru.dublgis.dgismobile.mapsdk.LonLat
import ru.dublgis.dgismobile.mapsdk.Map
import ru.dublgis.dgismobile.mapsdk.MapFragment as DGisMapFragment

const val LOCATION_PERMISSION_REQUEST_ID: Int = 777
const val LOC_PERM = Manifest.permission.ACCESS_FINE_LOCATION

abstract class MapActivity : AppCompatActivity() {

    private lateinit var locationProvider: FusedLocationProviderClient
    protected var map: Map? = null
    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        locationProvider = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment)
                as DGisMapFragment

        val apiKey = resources.getString(R.string.dgis_map_key)

        mapFragment.mapReadyCallback = this::onDGisMapReady
        mapFragment.setup(
            apiKey = apiKey,
            center = LonLat(55.30771, 25.20314),
            zoom = 12.0
        )

        val grant = ContextCompat.checkSelfPermission(this, LOC_PERM)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(LOC_PERM),
                LOCATION_PERMISSION_REQUEST_ID
            )
        } else {
            requestLocation()
        }

        mapOf(
            R.id.zoom_in to this::zoomInMap,
            R.id.zoom_out to this::zoomOutMap,
            R.id.location to this::centerMap

        ).forEach {
            val btn = findViewById<ImageButton>(it.key)
            btn.setOnClickListener(it.value)
        }
    }

    private fun onDGisMapReady(controller: Map?) {
        map = controller
        onDGisMapReady()
    }

    protected abstract fun onDGisMapReady()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_ID -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation()
                }
            }
        }
    }

    private fun requestLocation() {
        val grant = ContextCompat.checkSelfPermission(this, LOC_PERM)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val request = LocationRequest.create()
        request.interval = 60000
        request.fastestInterval = 20000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val listener = object : LocationCallback() {
            override fun onLocationResult(res: LocationResult?) {
                res?.lastLocation?.let {
                    this@MapActivity.location = it
                }
            }
        }
        locationProvider.requestLocationUpdates(request, listener, null)
    }

    private fun centerMap(@Suppress("UNUSED_PARAMETER") view: View?) {
        if (map == null || location == null) {
            return
        }

        map?.run {
            location?.let {
                center = LonLat(it.longitude, it.latitude)
                zoom = 16.0
            }
        }
    }

    private fun zoomInMap(@Suppress("UNUSED_PARAMETER") view: View?) {
        map?.run {
            zoom = zoom.inc()
        }
    }

    private fun zoomOutMap(@Suppress("UNUSED_PARAMETER") view: View?) {
        map?.run {
            zoom = zoom.dec()
        }
    }
}