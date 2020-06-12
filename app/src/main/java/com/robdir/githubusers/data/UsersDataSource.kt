package com.robdir.githubusers.data

import androidx.annotation.VisibleForTesting
import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import com.robdir.githubusers.data.users.model.UserDto
import com.robdir.githubusers.domain.UsersRepository
import javax.inject.Inject

@VisibleForTesting
const val RESULTS_PER_PAGE = 30

class UsersDataSource @Inject constructor(
    private val usersApi: UsersApi,
    private val usersCache: UsersCache
) : UsersRepository {

    override suspend fun getUsers(query: String): List<UserDto> =
        if (usersCache.getUsers(query).isEmpty()) {
            usersApi.getUsers(query, page = 1, resultsPerPage = RESULTS_PER_PAGE).users.also {
                usersCache.putUsers(query, it)
            }
        } else {
            usersCache.getUsers(query)
        }

    override suspend fun getUserDetail(username: String): UserDetailDto =
        usersApi.getUserDetail(username)

    override suspend fun updateUsers(query: String, users: List<UserDto>) {
        usersCache.putUsers(query, users)
    }
}
