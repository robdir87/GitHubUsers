package com.robdir.githubusers.data

import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import com.robdir.githubusers.data.users.model.UserDto
import com.robdir.githubusers.domain.UsersRepository
import javax.inject.Inject

private const val RESULTS_PER_PAGE = 25

class UsersDataSource @Inject constructor(
    private val usersApi: UsersApi
) : UsersRepository {

    override suspend fun getUsers(query: String, page: Int): List<UserDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetail(username: String): UserDetailDto {
        TODO("Not yet implemented")
    }
}
