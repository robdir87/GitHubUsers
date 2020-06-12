package com.robdir.githubusers.presentation.userdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.robdir.githubusers.GitHubUsersApplication
import com.robdir.githubusers.R
import com.robdir.githubusers.databinding.ActivityUserDetailBinding
import com.robdir.githubusers.domain.userdetail.UserDetail
import com.squareup.picasso.Picasso
import javax.inject.Inject

class UserDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var userDetailModelFactory: UserDetailViewModelFactory

    private lateinit var binding: ActivityUserDetailBinding

    private val userDetailViewModel: UserDetailViewModel by lazy { viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as GitHubUsersApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupUserDetailViewModel()

        displayUserBasicData()
        intent?.getStringExtra(EXTRA_USERNAME)?.let(userDetailViewModel::getUserDetail)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarUserDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun displayUserBasicData() {
        intent?.run {
            binding.toolbarLayoutUserDetail.title = getStringExtra(EXTRA_USERNAME)

            picasso.load(getStringExtra(EXTRA_AVATAR_URL))
                .noFade()
                .into(binding.imageViewUserAvatar)
        }
    }

    private fun setupUserDetailViewModel() {
        userDetailViewModel.viewState.observe(
            this@UserDetailActivity,
            Observer { viewState ->
                when (viewState) {
                    is UserDetailViewState.Loaded -> displayUserDetail(viewState.usersDetail)
                    is UserDetailViewState.Error ->
                        Toast.makeText(this@UserDetailActivity, "Error", Toast.LENGTH_SHORT).show()
                    is UserDetailViewState.NetworkError ->
                        Toast.makeText(this@UserDetailActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun displayUserDetail(userDetail: UserDetail) {
        with(binding.layoutUserDetail) {
            root.visibility = VISIBLE

            textViewUserFullName.text = userDetail.name
            textViewPublicRepos.text = userDetail.publicRepos
            textViewPublicGists.text = userDetail.publicGists
            textViewFollowers.text = userDetail.followers
            textViewFollowing.text = userDetail.following
        }

        binding.fabUserHomePage.apply {
            visibility = VISIBLE
            setOnClickListener { openUserHomePage(userDetail.htmlUrl) }
        }
    }

    private fun openUserHomePage(homeUrl: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(homeUrl)))
        } catch (exception: Exception) {
            Toast.makeText(this, R.string.no_browser_error_message, Toast.LENGTH_LONG).show()
        }
    }

    private fun viewModel(): UserDetailViewModel =
        ViewModelProvider(this, userDetailModelFactory).get(UserDetailViewModel::class.java)

    companion object {

        private const val EXTRA_USERNAME = "UserDetailActivity.EXTRA_USERNAME"
        private const val EXTRA_AVATAR_URL = "UserDetailActivity.EXTRA_AVATAR_URL"

        fun intent(context: Context, username: String, avatarUrl: String): Intent =
            Intent(context, UserDetailActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_AVATAR_URL, avatarUrl)
            }
    }
}
