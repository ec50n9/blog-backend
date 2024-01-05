package me.ec50n9.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExposedArticle (val id: Int, val title: String, val body: String)
