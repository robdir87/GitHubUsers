package com.robdir.githubusers

import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import com.robdir.githubusers.domain.userdetail.UserDetail

object MockDataProvider {

    fun userDetailDto(name: String? = "Roberto"): UserDetailDto =
        UserDetailDto(
            username = "robdir87",
            id = 87,
            avatarUrl = "avatar_url",
            htmlUrl = "html_url",
            name = name,
            publicRepos = 3,
            publicGists = 1,
            followers = 4,
            following = 5
        )

    fun userDetail(name: String = "Roberto"): UserDetail =
        UserDetail(
            username = "robdir87",
            id = 87,
            avatarUrl = "avatar_url",
            htmlUrl = "html_url",
            name = name,
            publicRepos = "3",
            publicGists = "1",
            followers = "4",
            following = "5"
        )
}
