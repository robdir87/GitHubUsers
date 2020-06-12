package com.robdir.githubusers.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.users.UserMapper
import com.robdir.githubusers.presentation.UsersViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userMapper: UserMapper
) : ViewModel() {

    private val _viewState = MutableLiveData<UsersViewState>()
    val viewState: LiveData<UsersViewState> = _viewState

    fun searchUsers(query: String) {
        if (query.isEmpty()){
            _viewState.value = UsersViewState.Loaded(users = emptyList())
            return
        }

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
            }
        }
    }

    private fun manageError(error: Throwable) {
        // TODO
    }
}
