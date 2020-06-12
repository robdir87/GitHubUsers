package com.robdir.githubusers.presentation.userdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.robdir.githubusers.GitHubUsersApplication
import com.robdir.githubusers.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        (application as GitHubUsersApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState, persistentState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {

        private const val BUNDLE_KEY_USERNAME = "UserDetailActivity.BUNDLE_KEY_USERNAME"
        private const val BUNDLE_KEY_AVATAR_URL = "UserDetailActivity.BUNDLE_KEY_AVATAR_URL"

        fun intent(context: Context, username: String, avatarUrl: String): Intent =
            Intent(context, UserDetailActivity::class.java).apply {
                putExtra(BUNDLE_KEY_USERNAME, username)
                putExtra(BUNDLE_KEY_AVATAR_URL, avatarUrl)
            }
    }
}
