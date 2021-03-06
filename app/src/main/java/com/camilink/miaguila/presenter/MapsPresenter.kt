package com.camilink.miaguila.presenter

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.camilink.miaguila.data.LatLongData
import com.camilink.miaguila.location.LocationRepo
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class MapsPresenter(private val view: View) : LocationRepo.LocationRepoListener, KoinComponent {

    private val locationRepo: LocationRepo by inject { parametersOf(this) }
    private val context: Application by inject()

    fun init() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationRepo.startLocationService()
        } else {
            view.requestLocationPermission()
        }
    }

    fun locationPermissionGranted() {
        locationRepo.startLocationService()
    }

    fun locationPermissionDenied() {
        view.showPermissionErrorMessage()
    }

    fun getFirstRoute() {
        locationRepo.getFirstRoute()
    }

    fun end() {
        locationRepo.stopLocationService()
    }

    //region RepoListener
    override fun showFirstRoute(points: ArrayList<LatLongData>) {
        view.showFirstRoute(points)
    }

    override fun updateLocation(location: Location) {
        view.updateLocation(location)
    }
    //endregion

    interface View {

        fun showFirstRoute(points: ArrayList<LatLongData>)

        fun requestLocationPermission()
        fun showPermissionErrorMessage()

        fun updateLocation(location: Location)

    }
}