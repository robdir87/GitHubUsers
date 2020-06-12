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
    fun `map should return a UserDetail with the name set to the username when a UserDetailDto is received and the name is null`() {
        assertEquals(userDetail(name = "robdir87"), mapper.map(userDetailDto(name = null)))
    }

    private fun userDetailDto(name: String? = "Roberto"): UserDetailDto =
        UserDetailDto(
            username = "robdir87",
            id = 87,
            avatarUrl = "avatar_url",
            htmlUrl = "html_url",
            name = name,
            publicRepos = 3,
            publicGists = 1,
            followers = 4,
            following = 5
        )

    private fun userDetail(name: String = "Roberto"): UserDetail =
        UserDetail(
            username = name,
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
