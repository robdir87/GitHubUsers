package com.robdir.githubusers.data

import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import com.robdir.githubusers.data.users.model.UserDto
import com.robdir.githubusers.data.users.model.UserListDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UsersDataSourceTest {

    private val usersApi = mockk<UsersApi>()
    private val usersCache = mockk<UsersCache>(relaxed = true)

    private val usersDataSource = UsersDataSource(usersApi, usersCache)

    private val query = "robdir87"

    // region getUsers
    @Test
    fun `getUsers should return a cached list of users when the cache contains an entry for the input query`() {
        runBlockingTest {
            // Arrange
            val userDtoList = listOf(UserDto(username = query, id = 87, avatarUrl = "avatar_url"))

            coEvery { usersCache.getUsers(query) } returns userDtoList

            // Act
            val dataSourceResult = usersDataSource.getUsers(query)

            // Assert
            assertEquals(userDtoList, dataSourceResult)
            coVerify(exactly = 0) { usersApi.getUsers(query, page = 1, resultsPerPage = RESULTS_PER_PAGE) }
        }
    }

    @Test(expected = IOException::class)
    fun `getUsers should throw an exception when the cache does not contain an entry for the input query and the api throw an Exception`() {
        runBlockingTest {
            // Arrange
            coEvery { usersCache.getUsers(query) } returns emptyList()
            coEvery { usersApi.getUsers(query, 1, RESULTS_PER_PAGE) } throws IOException()

            // Act
            usersDataSource.getUsers(query)
        }
    }

    @Test
    fun `getUsers should return a list of users and update the cache when  the cache does not contain an entry for the input query and the api succeeds`() {
        runBlockingTest {
            // Arrange
            val userDto = UserDto(username = query, id = 87, avatarUrl = "avatar_url")
            val userListDto = UserListDto(totalCount = 100, users = listOf(userDto))

            coEvery { usersCache.getUsers(query) } returns emptyList()
            coEvery { usersApi.getUsers(query, 1, RESULTS_PER_PAGE) } returns userListDto

            // Act
            val dataSourceResult = usersDataSource.getUsers(query)

            // Assert
            assertEquals(listOf(userDto), dataSourceResult)
            coVerify { usersCache.putUsers(query, listOf(userDto)) }
        }
    }
    // endregion

    // region getUserDetail
    @Test(expected = IOException::class)
    fun `getUserDetail should throw an exception when the usersApi throw an Exception`() {
        runBlockingTest {
            coEvery { usersApi.getUserDetail(username = query) } throws IOException()

            usersDataSource.getUserDetail(username = query)
        }
    }

    @Test
    fun `getUserDetail should return the user detail when the api succeeds`() {
        runBlockingTest {
            // Arrange
            val userDetailDto = UserDetailDto(
                username = query,
                id = 87,
                avatarUrl = "avatar_url",
                htmlUrl = "html_url",
                name = "Roberto",
                publicRepos = 2,
                publicGists = 0,
                followers = 3,
                following = 3
            )

            coEvery { usersApi.getUserDetail(username = query) } returns userDetailDto

            // Act
            val dataSourceResult = usersDataSource.getUserDetail(username = query)

            // Assert
            assertEquals(userDetailDto, dataSourceResult)
        }
    }
    // endregion
}
