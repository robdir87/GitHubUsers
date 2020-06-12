package com.robdir.githubusers.presentation.userdetail

import androidx.lifecycle.Observer
import com.robdir.githubusers.MockDataProvider.userDetail
import com.robdir.githubusers.MockDataProvider.userDetailDto
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.userdetail.UserDetail
import com.robdir.githubusers.domain.userdetail.UserDetailMapper
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
class UserDetailViewModelTest : BaseViewModelTest() {

    private val usersRepository = mockk<UsersRepository>()
    private val userDetailMapper = mockk<UserDetailMapper>()

    private val userDetailViewModel = UserDetailViewModel(usersRepository, userDetailMapper, networkInfoProvider)

    private val userDetailObserver = mockk<Observer<UserDetail>>(relaxed = true)

    private val username = "robdir87"

    @Before
    override fun setup() {
        super.setup()

        userDetailViewModel.apply {
            error.observeForever(errorObserver)
            userDetail.observeForever(userDetailObserver)
        }
    }

    @Test
    fun `getUserDetail should emit a network error when there is no network connection`() {
        runBlockingTest {
            // Arrange
            val error = GithubUsersError.Network

            coEvery { networkInfoProvider.isNetworkAvailable() } returns false
            coEvery { usersRepository.getUserDetail(username) } throws IOException()

            // Act
            userDetailViewModel.getUserDetail(username)

            // Assert
            assertEquals(error, userDetailViewModel.error.value)

            coVerify(exactly = 0) { userDetailObserver.onChanged(any()) }
            coVerify(exactly = 1) { errorObserver.onChanged(error) }
        }
    }

    @Test
    fun `getUserDetail should emit an error when retrieving the user detail fails`() {
        runBlockingTest {
            // Arrange
            val exception = Exception()
            val error = GithubUsersError.Generic(exception)

            coEvery { networkInfoProvider.isNetworkAvailable() } returns true
            coEvery { usersRepository.getUserDetail(username) } throws exception

            // Act
            userDetailViewModel.getUserDetail(username)

            // Assert
            assertEquals(error, userDetailViewModel.error.value)

            coVerify(exactly = 0) { userDetailObserver.onChanged(any()) }
            coVerify(exactly = 1) { errorObserver.onChanged(error) }
        }
    }

    @Test
    fun `getUserDetail should emit the user detail when retrieving the user detail succeeds`() {
        runBlockingTest {
            // Arrange
            val userDetailDto = userDetailDto()
            val userDetail = userDetail()

            coEvery { networkInfoProvider.isNetworkAvailable() } returns true
            coEvery { usersRepository.getUserDetail(username) } returns userDetailDto
            coEvery { userDetailMapper.map(userDetailDto) } returns userDetail

            // Act
            userDetailViewModel.getUserDetail(username)

            // Assert
            assertEquals(userDetail, userDetailViewModel.userDetail.value)

            coVerify(exactly = 1) { userDetailObserver.onChanged(userDetail) }
            coVerify(exactly = 0) { errorObserver.onChanged(any()) }
        }
    }
}
