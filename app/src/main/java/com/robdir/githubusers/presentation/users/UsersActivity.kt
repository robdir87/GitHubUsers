package com.robdir.githubusers.presentation.users

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SearchView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.robdir.githubusers.GitHubUsersApplication
import com.robdir.githubusers.R
import com.robdir.githubusers.databinding.ActivityUsersBinding
import com.robdir.githubusers.domain.users.User
import com.robdir.githubusers.presentation.GithubUsersError
import com.robdir.githubusers.presentation.userdetail.UserDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val SEARCH_THROTTLING_TIME_IN_MILLIS = 500L

class UsersActivity : AppCompatActivity(), CoroutineScope, UsersAdapter.Callback, UserTouchHelper.Callback {

    @Inject
    lateinit var usersAdapter: UsersAdapter

    @Inject
    lateinit var usersViewModelFactory: UsersViewModelFactory

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

    override fun onUserSelected(user: User) {
        startActivity(UserDetailActivity.intent(context = this, username = user.username, avatarUrl = user.avatarUrl))
    }

    override fun onUserSwiped(position: Int) {
        usersViewModel.removeUserFromSearchResults(
            userPosition = position,
            query = binding.searchViewUsers.query.toString()
        )
    }

    override fun onUserMoved(fromPosition: Int, toPosition: Int) {
        usersViewModel.swapUsersInSearchResults(
            firstUserPosition = fromPosition,
            secondUserPosition = toPosition,
            query = binding.searchViewUsers.query.toString()
        )
    }

    private fun setupRecyclerViewUsers() {
        usersAdapter.callback = this

        binding.recyclerViewUsers.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context)

            ItemTouchHelper(UserTouchHelper(this@UsersActivity)).attachToRecyclerView(this)
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
        usersViewModel.users.observe(
            this@UsersActivity,
            Observer { users ->
                binding.recyclerViewUsers.visibility = VISIBLE
                usersAdapter.submitList(users.toMutableList())
            }
        )

        usersViewModel.error.observe(
            this@UsersActivity,
            Observer { error ->
                when (error) {
                    is GithubUsersError.Generic -> manageError(R.string.users_not_available_error_message)
                    is GithubUsersError.Network -> manageError(R.string.network_error_message)
                }
            }
        )
    }

    private fun viewModel(): UsersViewModel =
        ViewModelProvider(this, usersViewModelFactory).get(UsersViewModel::class.java)

    private fun manageError(@StringRes messageId: Int) {
        binding.recyclerViewUsers.visibility = GONE
        binding.layoutNoUsers.textViewNoUsersMessage.setText(messageId)
    }
}
