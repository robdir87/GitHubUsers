package com.robdir.githubusers.di.modules

import com.robdir.githubusers.data.UsersApi
import com.robdir.githubusers.data.UsersDataSource
import com.robdir.githubusers.di.AppScope
import com.robdir.githubusers.domain.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class UsersModule {

    companion object {

        @Provides
        @AppScope
        fun usersApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)
    }

    @Binds
    @AppScope
    abstract fun usersRepository(usersDataSource: UsersDataSource): UsersRepository
}
