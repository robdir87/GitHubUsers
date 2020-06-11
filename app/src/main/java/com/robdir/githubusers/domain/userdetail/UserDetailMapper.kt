package com.robdir.githubusers.domain.userdetail

import com.robdir.githubusers.common.Mapper
import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import javax.inject.Inject

class UserDetailMapper @Inject constructor() : Mapper<UserDetailDto, UserDetail> {

    override fun map(from: UserDetailDto): UserDetail = with(from) {
        UserDetail(
            username = username,
            id = id,
            avatarUrl = avatarUrl,
            htmlUrl = htmlUrl,
            bio = bio,
            publicRepos = "$publicRepos",
            publicGists = "$publicGists",
            followers = "$followers",
            following = "$following"
        )
    }
}
