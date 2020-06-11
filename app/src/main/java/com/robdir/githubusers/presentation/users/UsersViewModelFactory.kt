package com.robdir.githubusers.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.users.UserMapper
import javax.inject.Inject

class UsersViewModelFactory @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userMapper: UserMapper
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        UsersViewModel(usersRepository, userMapper) as T
}
