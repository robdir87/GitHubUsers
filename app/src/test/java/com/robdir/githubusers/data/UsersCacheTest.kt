package com.robdir.githubusers.data

import com.robdir.githubusers.data.users.model.UserDto
import org.junit.Assert.assertEquals
import org.junit.Test

class UsersCacheTest {

    private val usersCache = UsersCache()

    private val query = "robdir87"

    @Test
    fun `getUsers should return an empty list if there is no user lists associated to the input query`() {
        assertEquals(emptyList<UserDto>(), usersCache.getUsers(query))
    }

    @Test
    fun `getUsers should return the list associated to the input query`() {
        // Arrange
        val userDtoList = listOf(UserDto(username = query, id = 87, avatarUrl = "avatar_url"))
        usersCache.cache[query] = userDtoList.toMutableList()

        // Act & Assert
        assertEquals(userDtoList, usersCache.getUsers(query))
    }

    @Test
    fun `putUsers should add the input list of user to the cache for the input query`() {
        // Arrange
        val userDto = UserDto(username = query, id = 87, avatarUrl = "avatar_url")
        val anotherUserDto = UserDto(username = "${query}123", id = 88, avatarUrl = "avatar_url_88")

        val userDtoList = listOf(userDto)
        usersCache.cache[query] = userDtoList.toMutableList()

        // Act
        usersCache.putUsers(query, listOf(userDto, anotherUserDto))

        // Assert
        assertEquals(listOf(userDto, anotherUserDto), usersCache.getUsers(query))
    }
}
