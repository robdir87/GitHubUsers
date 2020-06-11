package com.robdir.githubusers.presentation

import com.robdir.githubusers.domain.users.User

sealed class UsersViewState {

    object Loading : UsersViewState()

    object Error : UsersViewState()

    object NetworkError : UsersViewState()

    data class Loaded(val users: List<User>) : UsersViewState()
}
