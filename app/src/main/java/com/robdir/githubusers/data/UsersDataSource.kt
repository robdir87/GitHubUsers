package com.robdir.githubusers.data

import androidx.annotation.VisibleForTesting
import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import com.robdir.githubusers.data.users.model.UserDto
import com.robdir.githubusers.domain.UsersRepository
import javax.inject.Inject

@VisibleForTesting
const val RESULTS_PER_PAGE = 30

class UsersDataSource @Inject constructor(
    private val usersApi: UsersApi
) : UsersRepository {

    override suspend fun getUsers(query: String, page: Int): List<UserDto> =
        usersApi.getUsers(query, page, resultsPerPage = RESULTS_PER_PAGE).users

    override suspend fun getUserDetail(username: String): UserDetailDto =
        usersApi.getUserDetail(username)
}
