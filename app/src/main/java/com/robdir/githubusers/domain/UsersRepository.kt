package com.robdir.githubusers.domain

import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import com.robdir.githubusers.data.users.model.UserDto

interface UsersRepository {

    suspend fun getUsers(query: String): List<UserDto>

    suspend fun getUserDetail(username: String): UserDetailDto

    suspend fun updateUsers(query: String, users: List<UserDto>)
}
