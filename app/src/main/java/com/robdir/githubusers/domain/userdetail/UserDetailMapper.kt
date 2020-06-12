package com.robdir.githubusers.domain.userdetail

import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import javax.inject.Inject

class UserDetailMapper @Inject constructor() {

    fun map(userDetailDto: UserDetailDto): UserDetail = with(userDetailDto) {
        UserDetail(
            username = username,
            id = id,
            avatarUrl = avatarUrl,
            htmlUrl = htmlUrl,
            name = name ?: username,
            publicRepos = "$publicRepos",
            publicGists = "$publicGists",
            followers = "$followers",
            following = "$following"
        )
    }
}
