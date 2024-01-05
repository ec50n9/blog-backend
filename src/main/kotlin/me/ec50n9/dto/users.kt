package me.ec50n9.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExposedUser(val id: Int, val username: String, val nickname: String)

@Serializable
data class CreateUser(val username: String, val nickname: String, val password: String)

@Serializable
data class UpdateUser(val username: String?, val nickname: String?, val password: String?)
