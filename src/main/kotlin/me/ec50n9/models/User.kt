package me.ec50n9.models

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import me.ec50n9.dao.DatabaseSingleton.dbQuery
import org.jetbrains.exposed.sql.*

@Serializable
data class ExposedUser(val name: String, val age: Int)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)
    val age = integer("age")

    override val primaryKey = PrimaryKey(id)
}
