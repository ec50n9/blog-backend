package me.ec50n9.models

import org.jetbrains.exposed.sql.*

object Articles : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val body = varchar("body", 1024)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
