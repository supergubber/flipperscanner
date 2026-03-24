package com.example.proscan.routes

import com.example.proscan.models.responses.MessageResponse
import com.example.proscan.models.responses.PaginatedResponse
import com.example.proscan.plugins.userId
import com.example.proscan.service.DocumentService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import java.io.File

fun Route.documentRoutes() {
    route("/documents") {
        post("/upload") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val multipart = call.receiveMultipart()
            var fileName = "document"
            var scanType = "Document"
            var fileBytes: ByteArray? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        fileName = part.originalFileName ?: "document"
                        fileBytes = part.provider().toByteArray()
                    }
                    is PartData.FormItem -> {
                        when (part.name) {
                            "fileName" -> fileName = part.value
                            "scanType" -> scanType = part.value
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }

            val bytes = fileBytes ?: throw IllegalArgumentException("No file uploaded")
            val uploadDir = call.application.environment.config.property("storage.uploadDir").getString()
            val doc = DocumentService.upload(userId, fileName, scanType, bytes, uploadDir)
            call.respond(HttpStatusCode.Created, doc)
        }

        get {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
            val search = call.request.queryParameters["search"]
            val (docs, total) = DocumentService.list(userId, page, size, search)
            call.respond(PaginatedResponse(data = docs, page = page, size = size, total = total))
        }

        get("/{id}") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val docId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid document ID")
            val doc = DocumentService.getById(docId, userId)
            call.respond(doc)
        }

        get("/{id}/download") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val docId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid document ID")
            val filePath = DocumentService.getFilePath(docId, userId)
            val file = File(filePath)
            if (!file.exists()) throw NoSuchElementException("File not found on disk")
            call.respondFile(file)
        }

        delete("/{id}") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val docId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid document ID")
            DocumentService.delete(docId, userId)
            call.respond(MessageResponse(message = "Document deleted"))
        }
    }
}
