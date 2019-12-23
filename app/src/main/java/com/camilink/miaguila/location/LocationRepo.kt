package com.camilink.miaguila.location

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

class LocationRepo(private val listener: LocationListener) {

    fun getFirstRoute() {
        val routeOptions: PolylineOptions = PolylineOptions()
            .add(LatLng(4.667426, -74.056624))
            .add(LatLng(4.672655, -74.054071))

        listener.showFirstRoute(routeOptions)
    }

    interface LocationListener {
        fun showFirstRoute(polyOptions: PolylineOptions)
    }

}