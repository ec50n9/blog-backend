package me.ec50n9.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.ec50n9.dao.ArticleService
import me.ec50n9.dao.DatabaseSingleton

fun Application.articlesRouting() {
    val articleService = ArticleService(DatabaseSingleton.database)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/articles") {
            articleService.addNewArticle("新的文章", "这是内容哈哈哈")
            call.respond(HttpStatusCode.OK, articleService.allArticles())
        }
    }
}