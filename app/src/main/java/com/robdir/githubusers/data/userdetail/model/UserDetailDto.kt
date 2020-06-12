package com.robdir.githubusers.data.userdetail.model

import com.squareup.moshi.Json

data class UserDetailDto(
    @field:Json(name = "login") val username: String,
    val id: Int,
    @field:Json(name = "avatar_url") val avatarUrl: String,
    @field:Json(name = "html_url") val htmlUrl: String,
    val name: String,
    @field:Json(name = "public_repos") val publicRepos: Int,
    @field:Json(name = "public_gists") val publicGists: Int,
    val followers: Int,
    val following: Int
)
