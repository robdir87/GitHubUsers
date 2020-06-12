package com.robdir.githubusers.data

import androidx.annotation.VisibleForTesting
import com.robdir.githubusers.data.users.model.UserDto
import javax.inject.Inject

typealias Query = String

class UsersCache @Inject constructor() {
    @VisibleForTesting
    val cache = HashMap<Query, MutableList<UserDto>>()

    fun getUsers(query: String): List<UserDto> = cache[query].orEmpty()

    fun putUsers(query: String, users: List<UserDto>) {
        cache[query] = users.toMutableList()
    }
}
