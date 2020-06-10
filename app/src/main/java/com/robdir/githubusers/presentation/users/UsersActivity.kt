package com.robdir.githubusers.presentation.users

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.robdir.githubusers.GitHubUsersApplication
import com.robdir.githubusers.R
import com.robdir.githubusers.presentation.UsersViewState
import javax.inject.Inject

class SearchUsersActivity : AppCompatActivity() {

    @Inject
    lateinit var usersViewModelFactory: UsersViewModelFactory

    private val usersViewModel: UsersViewModel by lazy { viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as GitHubUsersApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_users)

        usersViewModel.apply {
            viewState.observe(
                this@SearchUsersActivity,
                Observer { viewState ->
                    when(viewState) {
                        is UsersViewState.Loading ->
                            Toast.makeText(this@SearchUsersActivity, "Loading", Toast.LENGTH_SHORT).show()
                        is UsersViewState.Loaded -> {
//                            Toast.makeText(this@SearchUsersActivity, "Users ${viewState.users}", Toast.LENGTH_LONG)
//                                .show()
                            findViewById<TextView>(R.id.textViewUsers).text = viewState.users.toString()
                        }
                        is UsersViewState.NetworkError ->
                            Toast.makeText(this@SearchUsersActivity, "Error", Toast.LENGTH_SHORT).show()
                        is UsersViewState.NetworkError ->
                            Toast.makeText(this@SearchUsersActivity, "Network Error", Toast.LENGTH_SHORT).show()
                    }

                }
            )
        }

        findViewById<TextView>(R.id.textViewGo).setOnClickListener { usersViewModel.searchUsers("robdir87") }
    }

    private fun viewModel(): UsersViewModel =
        ViewModelProvider(this, usersViewModelFactory).get(UsersViewModel::class.java)
}
