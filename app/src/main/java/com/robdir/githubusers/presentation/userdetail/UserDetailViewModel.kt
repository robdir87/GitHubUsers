package com.robdir.githubusers.presentation.userdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.userdetail.UserDetailMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userDetailMapper: UserDetailMapper
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
                //manageError(exception)
                exception.printStackTrace() // retrofit2.HttpException
                _viewState.value = UserDetailViewState.Error(exception)
            }
        }
    }
}
