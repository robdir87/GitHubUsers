package com.robdir.githubusers.data.users.model

import com.squareup.moshi.Json

data class UserDto(
    @field:Json(name = "login") val username: String,
    val id: Int,
    @field:Json(name = "avatar_url") val avatarUrl: String
)
