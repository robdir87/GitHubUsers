package com.robdir.githubusers.domain.users

import com.robdir.githubusers.data.users.model.UserDto
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {

    private val mapper = UserMapper()

    private val username = "robdir87"
    private val id = 87
    private val avatarUrl = "avatar_url"

    @Test
    fun `map should return a User when a UserDto is received`() {
        assertEquals(
            User(username, id, avatarUrl),
            mapper.map(UserDto(username, id, avatarUrl))
        )
    }

    @Test
    fun `mapList should return a list of User when a list of UserDto is received `() {
        assertEquals(
            listOf(User(username, id, avatarUrl)),
            mapper.mapList(listOf(UserDto(username, id, avatarUrl)))
        )
    }

    @Test
    fun `revertMap should return a UserDto when a User is received`() {
        assertEquals(
            UserDto(username, id, avatarUrl),
            mapper.revertMap(User(username, id, avatarUrl))
        )
    }

    @Test
    fun `revertMapList should return a list of UserDto when a list of User is received `() {
        assertEquals(
            listOf(UserDto(username, id, avatarUrl)),
            mapper.revertMapList(listOf(User(username, id, avatarUrl)))
        )
    }
}
