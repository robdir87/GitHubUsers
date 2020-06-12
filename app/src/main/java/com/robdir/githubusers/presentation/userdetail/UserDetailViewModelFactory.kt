package com.robdir.githubusers.presentation.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robdir.githubusers.core.NetworkInfoProvider
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.userdetail.UserDetailMapper
import javax.inject.Inject

class UserDetailViewModelFactory @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userDetailMapper: UserDetailMapper,
    private val networkInfoProvider: NetworkInfoProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        UserDetailViewModel(usersRepository, userDetailMapper, networkInfoProvider) as T
}
