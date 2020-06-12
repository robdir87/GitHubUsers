package com.robdir.githubusers.presentation

sealed class GithubUsersError {

    object Network : GithubUsersError()

    data class Generic(val cause: Exception) : GithubUsersError()
}
