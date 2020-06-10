package com.robdir.githubusers.di

import android.content.Context
import com.robdir.githubusers.GitHubUsersActivity
import com.robdir.githubusers.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component

@Component(modules = [NetworkModule::class])
@AppScope
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : AppComponent
    }

    fun inject(gitHubUsersActivity: GitHubUsersActivity)
}
