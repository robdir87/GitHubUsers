package com.robdir.githubusers.presentation.users

import com.robdir.githubusers.domain.users.User

sealed class UsersViewState {

    object Loading : UsersViewState()

    data class Error(val throwable: Throwable, val message: String? = null) : UsersViewState()

    object NetworkError : UsersViewState()

    data class Loaded(val users: List<User>) : UsersViewState()
}
