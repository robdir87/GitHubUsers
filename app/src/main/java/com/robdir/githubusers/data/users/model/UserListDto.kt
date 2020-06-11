package com.robdir.githubusers.data.users.model

import com.squareup.moshi.Json

data class UserListDto(
    @field:Json(name = "total_count") val totalCount: Int,
    @field:Json(name = "items") val users: List<UserDto>
)
