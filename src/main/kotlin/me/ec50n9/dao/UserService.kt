package me.ec50n9.dao

import me.ec50n9.models.ExposedUser
import me.ec50n9.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserService(database: Database) {

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun list(): List<ExposedUser> = DatabaseSingleton.dbQuery {
        Users.selectAll().map { ExposedUser(it[Users.name], it[Users.age]) }
    }

    suspend fun create(user: ExposedUser): Int = DatabaseSingleton.dbQuery {
        Users.insert {
            it[name] = user.name
            it[age] = user.age
        }[Users.id]
    }

    suspend fun read(id: Int): ExposedUser? = DatabaseSingleton.dbQuery {
        Users.select { Users.id eq id }
            .map { ExposedUser(it[Users.name], it[Users.age]) }
            .singleOrNull()
    }

    suspend fun update(id: Int, user: ExposedUser) {
        DatabaseSingleton.dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[age] = user.age
            }
        }
    }

    suspend fun delete(id: Int) {
        DatabaseSingleton.dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}
