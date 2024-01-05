package me.ec50n9

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.ec50n9.plugins.*
import me.ec50n9.routes.articlesRouting
import me.ec50n9.routes.usersRouting

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()

    usersRouting()
    articlesRouting()
}
