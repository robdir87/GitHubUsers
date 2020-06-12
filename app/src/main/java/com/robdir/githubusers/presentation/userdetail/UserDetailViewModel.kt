package com.robdir.githubusers.presentation.userdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robdir.githubusers.core.NetworkInfoProvider
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.userdetail.UserDetailMapper
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userDetailMapper: UserDetailMapper,
    private val networkInfoProvider: NetworkInfoProvider
) : ViewModel() {

    private val _viewState = MutableLiveData<UserDetailViewState>()
    val viewState: LiveData<UserDetailViewState> = _viewState

    fun getUserDetail(username: String) {
        viewModelScope.launch {
            try {
                _viewState.value = UserDetailViewState.Loaded(
                    usersDetail = userDetailMapper.map(usersRepository.getUserDetail(username))
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
                manageError(exception)
            }
        }
    }

    private fun manageError(error: Throwable) {
        if (error is IOException && !networkInfoProvider.isNetworkAvailable()) {
            _viewState.value = UserDetailViewState.NetworkError
        } else {
            _viewState.value = UserDetailViewState.Error(error)
        }
    }
}
