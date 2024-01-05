package me.ec50n9.models

import org.jetbrains.exposed.sql.*

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", length = 20)
    val nickname = varchar("nickname", length = 20)
    val password = varchar("password", length = 32)

    override val primaryKey = PrimaryKey(id)
}
