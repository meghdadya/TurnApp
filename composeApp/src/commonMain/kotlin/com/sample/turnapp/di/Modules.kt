package com.sample.turnapp.di

import com.sample.turnapp.core.core.utils.DispatcherProvider
import com.sample.turnapp.core.core.utils.PlatformDispatcherProvider
import com.sample.turnapp.core.data.network.NetworkCallAdapterFactory
import com.sample.turnapp.core.data.network.createHttpClient
import com.sample.turnapp.data.api.AppApi
import com.sample.turnapp.data.api.createAppApi
import com.sample.turnapp.data.repository.AppointmentsRepositoryImpl
import com.sample.turnapp.data.repository.PeopleRepositoryImpl
import com.sample.turnapp.data.source.AppointmentsDataSource
import com.sample.turnapp.data.source.AppointmentsDataSourceImpl
import com.sample.turnapp.data.source.PeopleDataSource
import com.sample.turnapp.data.source.PeopleDataSourceImpl
import com.sample.turnapp.feature.appointment.domain.usecase.DeleteAppointmentsUseCase
import com.sample.turnapp.feature.appointment.domain.usecase.GetAppointmentsUseCase
import com.sample.turnapp.feature.appointment.domain.usecase.RestoreAppointmentsUseCase
import com.sample.turnapp.feature.appointment.domain.usecase.SaveAppointmentUseCase
import com.sample.turnapp.feature.appointment.presentation.AppointmentsViewModel
import com.sample.turnapp.feature.appointment.domain.AppointmentsRepository
import com.sample.turnapp.feature.people.domain.PeopleRepository
import com.sample.turnapp.feature.people.domain.usecase.DeletePeopleUseCase
import com.sample.turnapp.feature.people.domain.usecase.GetPeopleListUseCase
import com.sample.turnapp.feature.people.domain.usecase.RestorePeopleUseCase
import com.sample.turnapp.feature.people.domain.usecase.SavePeopleUseCase
import com.sample.turnapp.feature.people.presentation.PeopleViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single {
        Ktorfit.Builder()
            .baseUrl(TURN_APP_BASE_URL)
            .httpClient(createHttpClient())
            .converterFactories(NetworkCallAdapterFactory())
            .build()
    }

    single<AppApi> {
        val ktorfit: Ktorfit = get()
        ktorfit.createAppApi()
    }

    single<DispatcherProvider> { PlatformDispatcherProvider() }

    factory<PeopleDataSource> { PeopleDataSourceImpl(get()) }
    factory<AppointmentsDataSource> { AppointmentsDataSourceImpl(get()) }

}

const val TURN_APP_BASE_URL = "https://fronttest.zhenic.com/api/"

val repositoryModule = module {
    factory<PeopleRepository> { PeopleRepositoryImpl(get(), get()) }
    factory<AppointmentsRepository> { AppointmentsRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    factory { DeletePeopleUseCase(get()) }
    factory { GetPeopleListUseCase(get()) }
    factory { RestorePeopleUseCase(get()) }
    factory { SavePeopleUseCase(get()) }
    factory { DeleteAppointmentsUseCase(get()) }
    factory { GetAppointmentsUseCase(get()) }
    factory { RestoreAppointmentsUseCase(get()) }
    factory { SaveAppointmentUseCase(get()) }
}

val viewmodelModule = module {
    viewModel{
        PeopleViewModel(get(),get(),get(),get(),get())
    }
    viewModel{
        AppointmentsViewModel(get(),get(),get(),get(),get())
    }
}