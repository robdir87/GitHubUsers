package com.robdir.githubusers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robdir.githubusers.core.NetworkInfoProvider
import java.io.IOException

abstract class BaseViewModel(private val networkInfoProvider: NetworkInfoProvider) : ViewModel() {

    private val _error = MutableLiveData<GitHubUsersError>()
    val error: LiveData<GitHubUsersError> = _error

    protected fun manageError(error: Exception) {
        if (error is IOException && !networkInfoProvider.isNetworkAvailable()) {
            _error.value = GitHubUsersError.Network
        } else {
            _error.value = GitHubUsersError.Generic(error)
        }
    }
}
