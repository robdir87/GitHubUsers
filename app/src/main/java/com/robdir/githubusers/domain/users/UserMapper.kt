package com.robdir.githubusers.domain.users

import com.robdir.githubusers.data.users.model.UserDto
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun map(userDto: UserDto): User = with(userDto) { User(username, id, avatarUrl) }

    fun mapList(userDtoList: List<UserDto>): List<User> = userDtoList.map(::map)

    fun revertMap(user: User): UserDto = with(user) { UserDto(username, id, avatarUrl) }

    fun revertMapList(userList: List<User>): List<UserDto> = userList.map(::revertMap)
}
