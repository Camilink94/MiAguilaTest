package com.camilink.miaguila.view

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.camilink.miaguila.R
import com.camilink.miaguila.data.LatLongData
import com.camilink.miaguila.presenter.MapsPresenter
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsPresenter.View {

    val presenter: MapsPresenter by inject { parametersOf(this) }
    val firstRoutePoints = arrayListOf<LatLongData>()
    val locationPoints = arrayListOf<Location>()

    private lateinit var mMap: GoogleMap

    companion object {
        const val REQUEST_LOCATION = 0
    }

    // region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        updateSpeed(0f)

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
    //endregion

    fun printLocations() {

        val builder = getFirstRouteBoundsBuilder()

        locationPoints.forEachIndexed { i, location ->

            builder.include(LatLng(location.latitude, location.longitude))

            val circleOptions = CircleOptions()
            circleOptions.center(LatLng(location.latitude, location.longitude))
            circleOptions.radius(10.toDouble())
            circleOptions.strokeColor(Color.BLACK)
            circleOptions.strokeWidth(1f)

            if (i == locationPoints.size - 1) {
                circleOptions.fillColor(resources.getColor(R.color.markerCurrent))
            } else {
                circleOptions.fillColor(resources.getColor(R.color.markerHistory))
            }

            mMap.addCircle(circleOptions)
        }

        val bounds = builder.build()

        val padding = 40
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        animateCamera(cu)
    }

    fun updateSpeed(speed: Float) {
        speedText.setText(getString(R.string.speed_txt, speed.toInt()))
    }

    private fun getFirstRouteBoundsBuilder(): LatLngBounds.Builder {

        val builder = LatLngBounds.Builder()
        this.firstRoutePoints.forEach {
            builder.include(LatLng(it.lat, it.long))
        }

        return builder
    }

    private fun animateCamera(cameraUpdate: CameraUpdate) {
        try {
            mMap.animateCamera(cameraUpdate)
        } catch (ex: IllegalStateException) {
            val mapView: View? = map.view
            if (mapView?.viewTreeObserver?.isAlive == true) {
                mapView.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            mapView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                            mMap.animateCamera(cameraUpdate)
                        }
                    })
            }
        }
    }

    //region View
    override fun showFirstRoute(points: ArrayList<LatLongData>) {

        this.firstRoutePoints.addAll(points)

        val routeOptions = PolylineOptions()
        this.firstRoutePoints.forEach { routeOptions.add(LatLng(it.lat, it.long)) }

        mMap.addPolyline(routeOptions)

        //Move camera to include route

        val builder = getFirstRouteBoundsBuilder()
        val bounds = builder.build()

        val padding = 250
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        animateCamera(cu)
    }

    override fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        }
    }

    override fun showPermissionErrorMessage() {
        Toast.makeText(this, R.string.error_permission_denied, Toast.LENGTH_LONG).show()
    }

    override fun updateLocation(location: Location) {
        locationPoints.add(location)
        printLocations()

        if (location.hasSpeed()) updateSpeed(location.speed)
    }
    //endregion
}
