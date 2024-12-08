package com.example

import com.example.cross.listenToSubscriptionEvents
import com.example.cross.save
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import io.ktor.server.response.*

fun main(args: Array<String>) {
    listenToSubscriptionEvents()
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureContentNegotiation()
    configureRouting()
}


private fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }
}

private fun Application.configureRouting() {
    routing {

        put("/clients/{client_id}/subscribe/") {

            val clientId = call.parameters["client_id"]

            if (clientId == null) {
                call.respond(BadRequest)
                return@put
            }

            save(clientId)
            call.respond(OK)
            return@put
        }
    }
}