package com.camilink.miaguila.injection

import com.camilink.miaguila.location.LocationRepo
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {

    factory { (listener: LocationRepo.LocationListener) -> LocationRepo(listener) }
}