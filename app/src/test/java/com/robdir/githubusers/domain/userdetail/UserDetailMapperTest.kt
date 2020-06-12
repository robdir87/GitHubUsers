package com.robdir.githubusers.domain.userdetail

import com.robdir.githubusers.MockDataProvider.userDetail
import com.robdir.githubusers.MockDataProvider.userDetailDto
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
}
