package com.camilink.miaguila.location

import com.camilink.miaguila.data.LatLongData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

class LocationRepo(private val listener: LocationListener) {

    fun getFirstRoute() {
        val points = arrayListOf<LatLongData>(
            LatLongData(lat = 4.667426, long = -74.056624),
            LatLongData(lat = 4.672655, long = -74.054071)
        )

        listener.showFirstRoute(points)
    }

    interface LocationListener {
        fun showFirstRoute(points: ArrayList<LatLongData>)
    }

}