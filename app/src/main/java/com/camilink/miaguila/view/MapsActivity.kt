package com.camilink.miaguila.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.camilink.miaguila.R
import com.camilink.miaguila.data.LatLongData
import com.camilink.miaguila.presenter.MapsPresenter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsPresenter.View {

    val presenter: MapsPresenter by inject { parametersOf(this) }
    val points = arrayListOf<LatLongData>()

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

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

    override fun showFirstRoute(points: ArrayList<LatLongData>) {

        this.points.addAll(points)

        val routeOptions = PolylineOptions()
        this.points.forEach { routeOptions.add(LatLng(it.lat, it.long)) }

        mMap.addPolyline(routeOptions)

    }
}
