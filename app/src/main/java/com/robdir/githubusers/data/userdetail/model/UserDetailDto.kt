package com.robdir.githubusers.data.userdetail.model

import com.squareup.moshi.Json

data class UserDetailDto(
    @Json(name = "login") val username: String,
    val id: Int,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "html_url") val htmlUrl: String,
    val bio: String,
    @Json(name = "public_repos") val publicRepos: Int,
    @Json(name = "public_gists") val publicGists: Int,
    val followers: Int,
    val following: Int
)
