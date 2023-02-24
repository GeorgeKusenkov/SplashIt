package com.example.splashit.app

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.example.splashit.data.room.AppDataBase
import com.example.splashit.di.AppComponent
import com.example.splashit.di.AppModule
import com.example.splashit.di.DaggerAppComponent

@ExperimentalPagingApi
class App: Application() {
    lateinit var db: AppDataBase
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

//        appComponent = DaggerAppComponent.create()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(context = this)).build()

        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "db"
        ).build()
    }
}