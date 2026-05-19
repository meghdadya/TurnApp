package com.sample.turnapp.di

import com.sample.turnapp.core.data.network.createHttpClient
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named(NETWORK_NAMED_MODULE)) {
        Ktorfit.Builder()
            .baseUrl(TURN_APP_BASE_URL)
            .httpClient(createHttpClient())
            .build()
    }
}

const val NETWORK_NAMED_MODULE = "network_named_module"
const val TURN_APP_BASE_URL = "https://fronttest.zhenic.com/api/"

val viewmodelModule = module {

}