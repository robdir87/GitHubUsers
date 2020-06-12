package com.robdir.githubusers.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robdir.githubusers.core.NetworkInfoProvider
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.users.User
import com.robdir.githubusers.domain.users.UserMapper
import com.robdir.githubusers.presentation.GithubUsersError
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Collections

class UsersViewModel constructor(
    private val usersRepository: UsersRepository,
    private val userMapper: UserMapper,
    private val networkInfoProvider: NetworkInfoProvider
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _error = MutableLiveData<GithubUsersError>()
    val error: LiveData<GithubUsersError> = _error

    fun searchUsers(query: String) {
        if (query.isEmpty()) {
            _users.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                _users.value = userMapper.mapList(usersRepository.getUsers(query))
            } catch (exception: Exception) {
                manageError(exception)
            }
        }
    }

    fun removeUserFromSearchResults(userPosition: Int, query: String) {
        viewModelScope.launch {
            _users.value?.toMutableList()?.run {
                if (userPosition >= 0) {
                    removeAt(userPosition)
                    usersRepository.updateUsers(query, userMapper.revertMapList(userList = this))
                    _users.value = this
                }
            }
        }
    }

    fun swapUsersInSearchResults(firstUserPosition: Int, secondUserPosition: Int, query: String) {
        viewModelScope.launch {
            _users.value?.toMutableList()?.run {
                if (firstUserPosition >= 0 && secondUserPosition >= 0 && firstUserPosition != secondUserPosition) {
                    Collections.swap(this, firstUserPosition, secondUserPosition)
                    usersRepository.updateUsers(query, userMapper.revertMapList(userList = this))
                    _users.value = this
                }
            }
        }
    }

    private fun manageError(error: Exception) {
        if (error is IOException && !networkInfoProvider.isNetworkAvailable()) {
            _error.value = GithubUsersError.Network
        } else {
            _error.value = GithubUsersError.Generic(error)
        }
    }
}
