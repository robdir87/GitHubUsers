package com.robdir.githubusers.presentation

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.robdir.githubusers.core.NetworkInfoProvider
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    protected val networkInfoProvider = mockk<NetworkInfoProvider>()

    protected val errorObserver = mockk<Observer<GitHubUsersError>>(relaxed = true)

    @Before
    @CallSuper
    open fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    @CallSuper
    open fun tearDown() {
        Dispatchers.resetMain()
    }
}
