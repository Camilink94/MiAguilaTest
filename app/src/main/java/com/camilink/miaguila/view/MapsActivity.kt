package com.camilink.miaguila.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.camilink.miaguila.R
import com.camilink.miaguila.presenter.MapsPresenter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsPresenter.View {

    val presenter: MapsPresenter by inject { parametersOf(this) }

    private lateinit var mMap: GoogleMap

    companion object {
        const val REQUEST_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        presenter.init()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        presenter.getFirstRoute()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        presenter.locationPermissionGranted()
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        presenter.locationPermissionDenied()
                    }
                }
            }
            else -> {
            }
        }
    }

    //region View
    override fun showFirstRoute(polyOptions: PolylineOptions) {
        mMap.addPolyline(polyOptions)
    }

    override fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        }
    }

    override fun showPermissionErrorMessage() {
        Toast.makeText(this, R.string.error_permission_denied, Toast.LENGTH_LONG).show()
    }
    //endregion
}
