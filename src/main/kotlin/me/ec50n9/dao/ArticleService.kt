package me.ec50n9.dao

import me.ec50n9.dto.ExposedArticle
import me.ec50n9.models.Articles
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ArticleService(database: Database) {

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

    suspend fun allArticles(): List<ExposedArticle> = DatabaseSingleton.dbQuery {
        Articles.selectAll().map(::resultRowToArticle)
    }

    suspend fun article(id: Int): ExposedArticle? = DatabaseSingleton.dbQuery {
        Articles
            .select { Articles.id eq id }
            .map(::resultRowToArticle)
            .singleOrNull()
    }

    suspend fun addNewArticle(title: String, body: String): ExposedArticle? = DatabaseSingleton.dbQuery {
        val insertStatement = Articles.insert {
            it[Articles.title] = title
            it[Articles.body] = body
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

    suspend fun editArticle(id: Int, title: String, body: String): Boolean = DatabaseSingleton.dbQuery {
        Articles.update({ Articles.id eq id }) {
            it[Articles.title] = title
            it[Articles.body] = body
        } > 0
    }

    suspend fun deleteArticle(id: Int): Boolean = DatabaseSingleton.dbQuery {
        Articles.deleteWhere { Articles.id eq id } > 0
    }
}