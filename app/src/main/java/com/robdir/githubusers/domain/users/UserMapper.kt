package com.robdir.githubusers.domain.users

import com.robdir.githubusers.common.Mapper
import com.robdir.githubusers.data.users.model.UserDto
import javax.inject.Inject

class UserMapper @Inject constructor() : Mapper<UserDto, User> {

    override fun map(from: UserDto): User = with(from) { User(username, id, avatarUrl) }
}
