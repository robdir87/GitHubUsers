package com.robdir.githubusers.data.users.model

import com.squareup.moshi.Json

data class UserListDto(
    @Json(name = "items") val users: List<UserDto>
)
