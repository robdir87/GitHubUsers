package com.robdir.githubusers.data

import com.robdir.githubusers.data.users.model.UserDto
import javax.inject.Inject

typealias Query = String

class UsersCache @Inject constructor() {
    private val cache = HashMap<Query, MutableList<UserDto>>()

    fun getUsers(query: String): List<UserDto> = cache[query].orEmpty()

    fun putUsers(query: String, users: List<UserDto>) {
        cache[query] = users.toMutableList()
    }
}
