package com.camilink.miaguila.location

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.camilink.miaguila.data.LatLongData
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocationRepo(private val listener: LocationRepoListener) : LocationListener, KoinComponent {

    private val locationManager: LocationManager by inject()

    @SuppressLint("MissingPermission")
    fun startLocationService() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0f, this)
    }

    fun stopLocationService() {
        locationManager.removeUpdates(this)
    }

    fun getFirstRoute() {
        val points = arrayListOf(
            LatLongData(lat = 4.667426, long = -74.056624),
            LatLongData(lat = 4.672655, long = -74.054071)
        )

        listener.showFirstRoute(points)
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
        fun showFirstRoute(points: ArrayList<LatLongData>)
    }

}