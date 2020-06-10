package com.robdir.githubusers.data.users.model

import com.squareup.moshi.Json

data class UserDto(
    @Json(name = "login") val username: String,
    val id: Int,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "html_url") val htmlUrl: String
)
