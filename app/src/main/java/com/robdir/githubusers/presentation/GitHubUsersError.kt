package com.robdir.githubusers.presentation

sealed class GitHubUsersError {

    object Network : GitHubUsersError()

    data class Generic(val cause: Exception) : GitHubUsersError()
}
