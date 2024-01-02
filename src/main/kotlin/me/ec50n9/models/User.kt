package me.ec50n9.models

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import me.ec50n9.dao.DatabaseSingleton.dbQuery
import org.jetbrains.exposed.sql.*

@Serializable
data class ExposedUser(val name: String, val age: Int)
class UserService(database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val age = integer("age")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun list(): List<ExposedUser> = dbQuery {
        Users.selectAll().map { ExposedUser(it[Users.name], it[Users.age]) }
    }

    suspend fun create(user: ExposedUser): Int = dbQuery {
        Users.insert {
            it[name] = user.name
            it[age] = user.age
        }[Users.id]
    }

    suspend fun read(id: Int): ExposedUser? = dbQuery {
            Users.select { Users.id eq id }
                    .map { ExposedUser(it[Users.name], it[Users.age]) }
                    .singleOrNull()
        }

    suspend fun update(id: Int, user: ExposedUser) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[age] = user.age
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}
