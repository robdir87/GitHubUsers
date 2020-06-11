package com.robdir.githubusers.domain.userdetail

data class UserDetail(
    val username: String,
    val id: Int,
    val avatarUrl: String,
    val htmlUrl: String,
    val bio: String,
    val publicRepos: String,
    val publicGists: String,
    val followers: String,
    val following: String
)
