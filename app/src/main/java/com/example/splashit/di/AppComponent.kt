package com.example.splashit.di

import androidx.paging.ExperimentalPagingApi
import com.example.splashit.presentation.authorization.AuthFragment
import com.example.splashit.presentation.curentuser.CurrentUserFragment
import com.example.splashit.presentation.details.DetailsFragment
import com.example.splashit.presentation.main.MainFragment
import com.example.splashit.presentation.usercollection.UserCollectionFragment
import dagger.Component

@ExperimentalPagingApi
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {
    fun inject(currentUserFragment: CurrentUserFragment)
    fun inject(mainFragment: MainFragment)
    fun inject(userCollectionFragment: UserCollectionFragment)
    fun inject(authFragment: AuthFragment)
    fun inject(detailsFragment: DetailsFragment)
}