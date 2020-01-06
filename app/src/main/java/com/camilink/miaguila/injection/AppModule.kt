package com.camilink.miaguila.injection

import android.content.Context
import android.location.LocationManager
import com.camilink.miaguila.location.LocationRepo
import com.camilink.miaguila.presenter.MapsPresenter
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {

    factory { (listener: LocationRepo.LocationRepoListener) -> LocationRepo(listener) }
    factory { (view: MapsPresenter.View) -> MapsPresenter(view) }
    factory { androidApplication() }
    factory { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }

}