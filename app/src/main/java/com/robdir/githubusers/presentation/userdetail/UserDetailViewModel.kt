package com.robdir.githubusers.presentation.userdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.robdir.githubusers.core.NetworkInfoProvider
import com.robdir.githubusers.domain.UsersRepository
import com.robdir.githubusers.domain.userdetail.UserDetail
import com.robdir.githubusers.domain.userdetail.UserDetailMapper
import com.robdir.githubusers.presentation.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userDetailMapper: UserDetailMapper,
    networkInfoProvider: NetworkInfoProvider
) : BaseViewModel(networkInfoProvider) {

    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> = _userDetail

    fun getUserDetail(username: String) {
        viewModelScope.launch {
            try {
                _userDetail.value = userDetailMapper.map(usersRepository.getUserDetail(username))
            } catch (exception: Exception) {
                manageError(exception)
            }
        }
    }
}
