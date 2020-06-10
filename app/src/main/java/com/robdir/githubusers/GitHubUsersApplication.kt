package com.robdir.githubusers

import android.app.Application
import com.robdir.githubusers.di.AppComponent
import com.robdir.githubusers.di.DaggerAppComponent

class GitHubUsersApplication : Application() {

    val appComponent: AppComponent by lazy { DaggerAppComponent.factory().create(applicationContext) }
}
