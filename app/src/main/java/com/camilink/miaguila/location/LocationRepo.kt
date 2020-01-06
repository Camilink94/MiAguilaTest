package com.camilink.miaguila.location

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocationRepo(private val listener: LocationRepoListener) : LocationListener, KoinComponent {

    private val locationManager: LocationManager by inject()

    @SuppressLint("MissingPermission")
    fun startLocationService() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f,this)
    }

    fun getFirstRoute() {
        val routeOptions: PolylineOptions = PolylineOptions()
            .add(LatLng(4.667426, -74.056624))
            .add(LatLng(4.672655, -74.054071))

        listener.showFirstRoute(routeOptions)
    }

    //region LocationListener
    override fun onLocationChanged(location: Location?) {

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }
    //endregion

    interface LocationRepoListener {
        fun showFirstRoute(polyOptions: PolylineOptions)
    }

}