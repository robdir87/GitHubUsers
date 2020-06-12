package com.robdir.githubusers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robdir.githubusers.core.NetworkInfoProvider
import java.io.IOException

abstract class BaseViewModel(private val networkInfoProvider: NetworkInfoProvider) : ViewModel() {

    private val _error = MutableLiveData<GithubUsersError>()
    val error: LiveData<GithubUsersError> = _error

    protected fun manageError(error: Exception) {
        if (error is IOException && !networkInfoProvider.isNetworkAvailable()) {
            _error.value = GithubUsersError.Network
        } else {
            _error.value = GithubUsersError.Generic(error)
        }
    }
}
