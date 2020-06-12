package com.robdir.githubusers.presentation.userdetail

import com.robdir.githubusers.domain.userdetail.UserDetail

sealed class UserDetailViewState {

    data class Error(val throwable: Throwable, val message: String? = null) : UserDetailViewState()

    object NetworkError : UserDetailViewState()

    data class Loaded(val usersDetail: UserDetail) : UserDetailViewState()
}
