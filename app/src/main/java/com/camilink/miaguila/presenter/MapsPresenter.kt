package com.camilink.miaguila.presenter

import com.camilink.miaguila.location.LocationRepo
import com.google.android.gms.maps.model.PolylineOptions
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class MapsPresenter(private val view: View) : LocationRepo.LocationListener, KoinComponent {

    private val locationRepo: LocationRepo by inject { parametersOf(this) }

    fun getFirstRoute() {
        locationRepo.getFirstRoute()
    }

    override fun showFirstRoute(polyOptions: PolylineOptions) {
        view.showFirstRoute(polyOptions)
    }

    interface View {
        fun showFirstRoute(polyOptions: PolylineOptions)
    }
}