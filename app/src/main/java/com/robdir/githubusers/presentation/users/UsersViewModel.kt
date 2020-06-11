package com.robdir.githubusers.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.users.User
import com.robdir.githubusers.domain.users.UserMapper
import com.robdir.githubusers.presentation.UsersViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userMapper: UserMapper
) : ViewModel() {

    // region LiveData
    private val _viewState = MutableLiveData<UsersViewState>()
    val viewState: LiveData<UsersViewState> = _viewState

//    private val _error = MutableLiveData<Throwable>()
//    val error: LiveData<Throwable> = _error
//
//    private val _networkError = MutableLiveData<Throwable>()
//    val networkError: LiveData<Throwable> = _networkError
//
//    private val _users = MutableLiveData<List<User>>()
//    val users: LiveData<List<User>> = _users

    // endregion

    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                _viewState.value = UsersViewState.Loading
                _viewState.value = UsersViewState.Loaded(
                    users = userMapper.mapList(usersRepository.getUsers(query, page = 1)) // TODO: add pagination
                )
            } catch(exception: Exception) {
                //manageError(exception)
                exception.printStackTrace() // retrofit2.HttpException
                _viewState.value = UsersViewState.Error
            } finally {
//                _loading.value = false
            }
        }
    }

    private fun manageError(error: Throwable) {
        // TODO
    }
}
