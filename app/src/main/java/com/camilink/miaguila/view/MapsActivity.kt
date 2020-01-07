package com.camilink.miaguila.view

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.camilink.miaguila.R
import com.camilink.miaguila.data.LatLongData
import com.camilink.miaguila.presenter.MapsPresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsPresenter.View {

    val presenter: MapsPresenter by inject { parametersOf(this) }
    val points = arrayListOf<LatLongData>()

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

    override fun onDestroy() {
        super.onDestroy()
        presenter.end()
    }

    //region View
    override fun showFirstRoute(points: ArrayList<LatLongData>) {

        this.points.addAll(points)

        val routeOptions = PolylineOptions()
        this.points.forEach { routeOptions.add(LatLng(it.lat, it.long)) }

        mMap.addPolyline(routeOptions)

        //Move camera to include route

        val builder = LatLngBounds.Builder()
        this.points.forEach {
            builder.include(LatLng(it.lat, it.long))
        }
        val bounds = builder.build();

        val padding = 250
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        try {
            mMap.animateCamera(cu)
        } catch (ex: IllegalStateException) {
            val mapView: View? = map.view
            if (mapView?.viewTreeObserver?.isAlive == true) {
                mapView.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            mapView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                            mMap.animateCamera(cu)
                        }
                    })
            }
        }
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
