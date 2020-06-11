package com.robdir.githubusers.common

interface Mapper<From, To> {

    fun map(from: From): To

    fun mapList(from: List<From>): List<To> = from.map(::map)
}
