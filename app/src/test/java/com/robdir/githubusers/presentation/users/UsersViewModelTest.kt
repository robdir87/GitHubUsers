package com.robdir.githubusers.presentation.users

import androidx.lifecycle.Observer
import com.robdir.githubusers.data.users.model.UserDto
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.users.User
import com.robdir.githubusers.domain.users.UserMapper
import com.robdir.githubusers.presentation.BaseViewModelTest
import com.robdir.githubusers.presentation.GithubUsersError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UsersViewModelTest : BaseViewModelTest() {

    private val usersRepository = mockk<UsersRepository>(relaxed = true)
    private val userMapper = mockk<UserMapper>()

    private val usersViewModel = UsersViewModel(usersRepository, userMapper, networkInfoProvider)

    private val usersObserver = mockk<Observer<List<User>>>(relaxed = true)

    private val query = "robdir87"

    @Before
    override fun setup() {
        super.setup()

        usersViewModel.apply {
            error.observeForever(errorObserver)
            users.observeForever(usersObserver)
        }
    }

    // region searchUsers
    @Test
    fun `searchUsers should emit a network error when there is no network connection`() {
        runBlockingTest {
            // Arrange
            val error = GithubUsersError.Network

            coEvery { networkInfoProvider.isNetworkAvailable() } returns false
            coEvery { usersRepository.getUsers(query) } throws IOException()

            // Act
            usersViewModel.searchUsers(query)

            // Assert
            assertEquals(error, usersViewModel.error.value)

            coVerify(exactly = 0) { usersObserver.onChanged(any()) }
            coVerify(exactly = 1) { errorObserver.onChanged(error) }
        }
    }

    @Test
    fun `searchUsers should emit an error when retrieving the users fails`() {
        runBlockingTest {
            // Arrange
            val exception = Exception()
            val error = GithubUsersError.Generic(exception)

            coEvery { networkInfoProvider.isNetworkAvailable() } returns true
            coEvery { usersRepository.getUsers(query) } throws exception

            // Act
            usersViewModel.searchUsers(query)

            // Assert
            assertEquals(error, usersViewModel.error.value)

            coVerify(exactly = 0) { usersObserver.onChanged(any()) }
            coVerify(exactly = 1) { errorObserver.onChanged(error) }
        }
    }

    @Test
    fun `searchUsers should emit an empty user list without performing any search if the query is empty`() {
        runBlockingTest {
            // Act
            usersViewModel.searchUsers(query = "")

            // Assert
            assertEquals(emptyList<User>(), usersViewModel.users.value)

            coVerify(exactly = 0) { usersRepository.getUsers(query = "") }
            coVerify(exactly = 1) { usersObserver.onChanged(emptyList()) }
            coVerify(exactly = 0) { errorObserver.onChanged(any()) }
        }
    }

    @Test
    fun `searchUsers should emit the users when retrieving the user detail succeeds`() {
        runBlockingTest {
            // Arrange
            val userDtoList = listOf(UserDto(username = query, id = 87, avatarUrl = "avatar_url"))
            val userList = listOf(User(username = query, id = 87, avatarUrl = "avatar_url"))

            coEvery { networkInfoProvider.isNetworkAvailable() } returns true
            coEvery { usersRepository.getUsers(query) } returns userDtoList
            coEvery { userMapper.mapList(userDtoList) } returns userList

            // Act
            usersViewModel.searchUsers(query)

            // Assert
            assertEquals(userList, usersViewModel.users.value)

            coVerify(exactly = 1) { usersObserver.onChanged(userList) }
            coVerify(exactly = 0) { errorObserver.onChanged(any()) }
        }
    }
    // endregion

    // region removeUserFromSearchResults
    @Test
    fun `removeUserFromSearchResults should not emit anything if the user position is negative`() {
        testSwapUsersInSearchResultsNotEmitting(userPosition = -1)
    }

    @Test
    fun `removeUserFromSearchResults should not emit anything if the user position is beyond the users size`() {
        testSwapUsersInSearchResultsNotEmitting(userPosition = 1)
    }

    @Test
    fun `removeUserFromSearchResults should remove the user from the search results and emit the updated list of users if the user position is valid`() {
        runBlockingTest {
            // Arrange
            val userDtoList = listOf(UserDto(username = query, id = 87, avatarUrl = "avatar_url"))
            val userList = listOf(User(username = query, id = 87, avatarUrl = "avatar_url"))

            coEvery { usersRepository.getUsers(query) } returns userDtoList
            coEvery { userMapper.mapList(userDtoList) } returns userList
            coEvery { userMapper.revertMapList(emptyList()) } returns emptyList()

            // Act
            usersViewModel.removeUserFromSearchResults(userPosition = 0, query = query)

            // Assert
            assertEquals(emptyList<User>(), usersViewModel.users.value)

            coVerify(exactly = 1) { usersObserver.onChanged(emptyList()) }
            coVerify(exactly = 1) { usersRepository.updateUsers(query, emptyList()) }
        }
    }

    private fun testSwapUsersInSearchResultsNotEmitting(userPosition: Int) {
        runBlockingTest {
            // Arrange
            val userDtoList = listOf(UserDto(username = query, id = 87, avatarUrl = "avatar_url"))
            val userList = listOf(User(username = query, id = 87, avatarUrl = "avatar_url"))

            coEvery { usersRepository.getUsers(query) } returns userDtoList
            coEvery { userMapper.mapList(userDtoList) } returns userList

            // Act
            usersViewModel.removeUserFromSearchResults(userPosition = userPosition, query = query)

            // Assert
            coVerify(exactly = 0) { usersObserver.onChanged(any()) }
            coVerify(exactly = 0) { usersRepository.updateUsers(any(), any()) }
        }
    }
    // endregion

    // region swapUsersInSearchResults
    @Test
    fun `swapUsersInSearchResults should not emit anything if the first user position is negative`() {
        testSwapUsersInSearchResultsNotEmitting(firstUserPosition = -1, secondUserPosition = 0)
    }

    @Test
    fun `swapUsersInSearchResults should not emit anything if the first user position is beyond the users size`() {
        testSwapUsersInSearchResultsNotEmitting(firstUserPosition = 2, secondUserPosition = 0)
    }

    @Test
    fun `swapUsersInSearchResults should not emit anything if the second user position is negative`() {
        testSwapUsersInSearchResultsNotEmitting(firstUserPosition = 1, secondUserPosition = -1)
    }

    @Test
    fun `swapUsersInSearchResults should not emit anything if the second user position is beyond the users size`() {
        testSwapUsersInSearchResultsNotEmitting(firstUserPosition = 1, secondUserPosition = 2)
    }

    @Test
    fun `swapUsersInSearchResults should not emit anything if the the first and the second user positions are the same`() {
        testSwapUsersInSearchResultsNotEmitting(firstUserPosition = 1, secondUserPosition = 1)
    }

    @Test
    fun `swapUsersInSearchResults should swap the users in the search results and emit the updated list if the the first and the second user positions are valid and not the same`() {
        runBlockingTest {
            // Arrange
            val userDtoList = listOf(
                UserDto(username = query, id = 87, avatarUrl = "avatar_url"),
                UserDto(username = "${query}123", id = 88, avatarUrl = "avatar_url_88")
            )
            val userList = listOf(
                User(username = query, id = 87, avatarUrl = "avatar_url"),
                User(username = "${query}123", id = 88, avatarUrl = "avatar_url_88")
            )
            val swappedUserDtoList = listOf(
                UserDto(username = "${query}123", id = 88, avatarUrl = "avatar_url_88"),
                UserDto(username = query, id = 87, avatarUrl = "avatar_url")
            )
            val swappedUserList = listOf(
                User(username = "${query}123", id = 88, avatarUrl = "avatar_url_88"),
                User(username = query, id = 87, avatarUrl = "avatar_url")
            )

            coEvery { usersRepository.getUsers(query) } returns userDtoList
            coEvery { userMapper.mapList(userDtoList) } returns userList
            coEvery { userMapper.revertMapList(swappedUserList) } returns swappedUserDtoList

            // Act
            usersViewModel.swapUsersInSearchResults(firstUserPosition = 0, secondUserPosition = 1, query = query)

            // Assert
            assertEquals(swappedUserList, usersViewModel.users.value)

            coVerify(exactly = 1) { usersObserver.onChanged(swappedUserList) }
            coVerify(exactly = 1) { usersRepository.updateUsers(query, swappedUserDtoList) }
        }
    }

    private fun testSwapUsersInSearchResultsNotEmitting(firstUserPosition: Int, secondUserPosition: Int) {
        runBlockingTest {
            // Arrange
            val userDtoList = listOf(
                UserDto(username = query, id = 87, avatarUrl = "avatar_url"),
                UserDto(username = "${query}123", id = 88, avatarUrl = "avatar_url_88")
            )
            val userList = listOf(
                User(username = query, id = 87, avatarUrl = "avatar_url"),
                User(username = "${query}123", id = 88, avatarUrl = "avatar_url_88")
            )

            coEvery { usersRepository.getUsers(query) } returns userDtoList
            coEvery { userMapper.mapList(userDtoList) } returns userList

            // Act
            usersViewModel.swapUsersInSearchResults(
                firstUserPosition = firstUserPosition,
                secondUserPosition = secondUserPosition,
                query = query
            )

            // Assert
            coVerify(exactly = 0) { usersObserver.onChanged(any()) }
            coVerify(exactly = 0) { usersRepository.updateUsers(any(), any()) }
        }
    }
    // endregion
}
