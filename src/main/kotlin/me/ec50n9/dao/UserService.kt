package me.ec50n9.dao

import me.ec50n9.dto.CreateUser
import me.ec50n9.dto.ExposedUser
import me.ec50n9.dto.UpdateUser
import me.ec50n9.models.Users
import org.h2.engine.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserService(database: Database) {

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    private fun convert2ExposedUser(user: ResultRow) =
        ExposedUser(user[Users.id], user[Users.username], user[Users.nickname])


    suspend fun list(): List<ExposedUser> = DatabaseSingleton.dbQuery {
        Users.selectAll().map(::convert2ExposedUser)
    }

    suspend fun create(user: CreateUser): Int = DatabaseSingleton.dbQuery {
        Users.insert {
            it[username] = user.username
            it[nickname] = user.nickname
            it[password] = user.password
        }[Users.id]
    }

    suspend fun read(id: Int): ExposedUser? = DatabaseSingleton.dbQuery {
        Users.select { Users.id eq id }
            .map(::convert2ExposedUser)
            .singleOrNull()
    }

    suspend fun update(id: Int, user: UpdateUser) {
        DatabaseSingleton.dbQuery {
            Users.update({ Users.id eq id }) {
                user.username?.let { v -> it[username] = v }
                user.nickname?.let { v -> it[nickname] = v }
                user.password?.let { v -> it[password] = v }
            }
        }
    }

    suspend fun delete(id: Int) {
        DatabaseSingleton.dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}
