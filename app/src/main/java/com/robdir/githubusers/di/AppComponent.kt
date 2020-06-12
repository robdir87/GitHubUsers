package com.robdir.githubusers.di

import android.content.Context
import com.robdir.githubusers.presentation.users.UsersActivity
import com.robdir.githubusers.di.modules.NetworkModule
import com.robdir.githubusers.di.modules.UsersModule
import com.robdir.githubusers.presentation.userdetail.UserDetailActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [NetworkModule::class, UsersModule::class])
@AppScope
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(usersActivity: UsersActivity)
    fun inject(userDetailActivity: UserDetailActivity)
}
