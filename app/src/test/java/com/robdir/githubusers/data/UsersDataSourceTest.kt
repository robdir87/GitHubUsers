package com.robdir.githubusers.data

import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import com.robdir.githubusers.data.users.model.UserDto
import com.robdir.githubusers.data.users.model.UserListDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UsersDataSourceTest {

    private val usersApi = mockk<UsersApi>()

    private val usersDataSource = UsersDataSource(usersApi)

    private val query = "robdir87"

    // region getUsers
    @Test(expected = IOException::class)
    fun `getUsers should throw an exception when the usersApi throw an Exception`() {
        runBlockingTest {
            // Arrange
            val page = 1
            coEvery { usersApi.getUsers(query, page, RESULTS_PER_PAGE) } throws IOException()

            // Act
            usersDataSource.getUsers(query, page)
        }
    }

    @Test
    fun `getUsers should return a list of users when the api succeeds`() {
        runBlockingTest {
            // Arrange
            val page = 1
            val userDto = UserDto(username = query, id = 87, avatarUrl = "avatar_url")
            val userListDto = UserListDto(totalCount = 100, users = listOf(userDto))

            coEvery { usersApi.getUsers(query, page, RESULTS_PER_PAGE) } returns userListDto

            // Act
            val dataSourceResult = usersDataSource.getUsers(query, page)

            // Assert
            assertEquals(listOf(userDto), dataSourceResult)
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
                bio = "bio",
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
