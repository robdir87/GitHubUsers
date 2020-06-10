package com.robdir.githubusers.data

import com.robdir.githubusers.data.userdetail.model.UserDetailDto
import com.robdir.githubusers.data.users.model.UserListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {

    @GET("search/users")
    suspend fun getUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") resultsPerPage: Int
    ): UserListDto

    @GET("/users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserDetailDto
}
