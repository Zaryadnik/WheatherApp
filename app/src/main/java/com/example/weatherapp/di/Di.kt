package com.example.weatherapp.di

import com.example.weatherapp.repository.DataStoreRepository
import com.example.weatherapp.repository.Repository
import com.example.weatherapp.viewModels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

var diModule = module{
    single{
        Repository()
    }
    single {
        DataStoreRepository(get())
    }
    viewModelOf(::MainViewModel)
}