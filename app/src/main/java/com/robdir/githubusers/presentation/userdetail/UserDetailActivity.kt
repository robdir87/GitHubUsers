package com.robdir.githubusers.presentation.userdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.robdir.githubusers.GitHubUsersApplication
import com.robdir.githubusers.databinding.ActivityUserDetailBinding
import com.squareup.picasso.Picasso
import javax.inject.Inject

class UserDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var picasso: Picasso

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as GitHubUsersApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        displayUserBasicData()
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
