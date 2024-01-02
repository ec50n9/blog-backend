package me.ec50n9.models

import kotlinx.serialization.Serializable
import me.ec50n9.dao.DatabaseSingleton.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ExposedArticle (val id: Int, val title: String, val body: String)
class ArticleService(database: Database) {
    object Articles : Table() {
        val id = integer("id").autoIncrement()
        val title = varchar("title", 128)
        val body = varchar("body", 1024)

        override val primaryKey: PrimaryKey = PrimaryKey(id)
    }

    init{
        transaction(database){
            SchemaUtils.create(Articles)
        }
    }

    private fun resultRowToArticle(row: ResultRow) = ExposedArticle(
        id = row[Articles.id],
        title = row[Articles.title],
        body = row[Articles.body]
    )

     suspend fun allArticles(): List<ExposedArticle> = dbQuery {
        Articles.selectAll().map(::resultRowToArticle)
    }

     suspend fun article(id: Int): ExposedArticle? = dbQuery {
        Articles
            .select { Articles.id eq id }
            .map(::resultRowToArticle)
            .singleOrNull()
    }

     suspend fun addNewArticle(title: String, body: String): ExposedArticle? = dbQuery {
        val insertStatement = Articles.insert {
            it[Articles.title] = title
            it[Articles.body] = body
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

     suspend fun editArticle(id: Int, title: String, body: String): Boolean = dbQuery {
        Articles.update({ Articles.id eq id }) {
            it[Articles.title] = title
            it[Articles.body] = body
        } > 0
    }

     suspend fun deleteArticle(id: Int): Boolean = dbQuery {
        Articles.deleteWhere { Articles.id eq id } > 0
    }
}