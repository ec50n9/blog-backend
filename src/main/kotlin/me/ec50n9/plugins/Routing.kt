package me.ec50n9.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.ec50n9.dao.DatabaseSingleton.database
import me.ec50n9.models.ArticleService
import me.ec50n9.models.ExposedUser
import me.ec50n9.models.UserService

fun Application.configureRouting() {
    val userService = UserService(database)
    val articleService = ArticleService(database)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/articles") {
            articleService.addNewArticle("新的文章", "这是内容哈哈哈")
            call.respond(HttpStatusCode.OK, articleService.allArticles())
        }
    }

    routing {
        get("/users"){
            val users = userService.list()
            call.respond(HttpStatusCode.OK, users)
        }
        // Create user
        post("/users") {
            val user = call.receive<ExposedUser>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
