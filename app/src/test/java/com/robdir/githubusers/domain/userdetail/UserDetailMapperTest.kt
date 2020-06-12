package com.robdir.githubusers.domain.userdetail

import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import org.junit.Assert.assertEquals
import org.junit.Test

class UserDetailMapperTest {

    private val mapper = UserDetailMapper()

    @Test
    fun `map should return a UserDetail when a UserDetailDto is received`() {
        assertEquals(userDetail(), mapper.map(userDetailDto()))
    }

    @Test
    fun `mapList should return a list of User when a list of UserDto is received `() {
        assertEquals(listOf(userDetail()), mapper.mapList(listOf(userDetailDto())))
    }

    private fun userDetailDto(): UserDetailDto =
        UserDetailDto(
            username = "robdir87",
            id = 87,
            avatarUrl = "avatar_url",
            htmlUrl = "html_url",
            name = "Roberto",
            publicRepos = 3,
            publicGists = 1,
            followers = 4,
            following = 5
        )

    private fun userDetail(): UserDetail =
        UserDetail(
            username = "robdir87",
            id = 87,
            avatarUrl = "avatar_url",
            htmlUrl = "html_url",
            name = "Roberto",
            publicRepos = "3",
            publicGists = "1",
            followers = "4",
            following = "5"
        )
}
