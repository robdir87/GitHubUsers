package com.robdir.githubusers.presentation.users

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.robdir.githubusers.GitHubUsersApplication
import com.robdir.githubusers.databinding.ActivityUsersBinding
import com.robdir.githubusers.domain.users.User
import com.robdir.githubusers.presentation.UsersViewState
import com.robdir.githubusers.presentation.userdetail.UserDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val SEARCH_THROTTLING_TIME_IN_MILLIS = 500L

class UsersActivity : AppCompatActivity(), CoroutineScope, UsersAdapter.Callback {

    @Inject
    lateinit var usersViewModelFactory: UsersViewModelFactory

    @Inject
    lateinit var usersAdapter: UsersAdapter

    private lateinit var binding: ActivityUsersBinding

    private val usersViewModel: UsersViewModel by lazy { viewModel() }

    private var queryTextChangedJob: Job? = null
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as GitHubUsersApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViewUsers()
        setupSearchViewUsers()
        setupUsersViewModel()
    }

    override fun onDestroy() {
        queryTextChangedJob?.cancel()
        super.onDestroy()
    }

    override fun onUserSelected(username: String, avatarUrl: String) {
        Toast.makeText(this, "TODO: Selected $username", Toast.LENGTH_SHORT).show()

        startActivity(UserDetailActivity.intent(context = this, username = username, avatarUrl = avatarUrl))
    }

    private fun setupRecyclerViewUsers() {
        usersAdapter.callback = this

        binding.recyclerViewUsers.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupSearchViewUsers() {
        binding.searchViewUsers.run {
            isIconifiedByDefault = false

            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(query: String?): Boolean {
                        queryTextChangedJob?.cancel()

                        queryTextChangedJob = launch {
                            delay(SEARCH_THROTTLING_TIME_IN_MILLIS)
                            usersViewModel.searchUsers(query.orEmpty())
                        }
                        return true
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        clearFocus()
                        return true
                    }
                }
            )
        }
    }

    private fun setupUsersViewModel() {
        usersViewModel.viewState.observe(
            this@UsersActivity,
            Observer { viewState ->
                when (viewState) {
                    is UsersViewState.Loading ->
                        Toast.makeText(this@UsersActivity, "Loading", Toast.LENGTH_SHORT).show()
                    is UsersViewState.Loaded ->
                        usersAdapter.submitList(viewState.users)
                    is UsersViewState.Error ->
                        Toast.makeText(this@UsersActivity, "Error", Toast.LENGTH_SHORT).show()
                    is UsersViewState.NetworkError ->
                        Toast.makeText(this@UsersActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun viewModel(): UsersViewModel =
        ViewModelProvider(this, usersViewModelFactory).get(UsersViewModel::class.java)
}
