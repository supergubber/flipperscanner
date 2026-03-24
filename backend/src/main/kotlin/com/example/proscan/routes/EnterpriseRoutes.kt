package com.example.proscan.routes

import com.example.proscan.models.requests.CreateTeamMemberRequest
import com.example.proscan.models.requests.UpdateSecurityRequest
import com.example.proscan.models.requests.UpdateTeamMemberRequest
import com.example.proscan.models.responses.MessageResponse
import com.example.proscan.models.responses.PaginatedResponse
import com.example.proscan.service.EnterpriseService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.enterpriseRoutes() {
    route("/enterprise") {
        get("/dashboard") {
            val dashboard = EnterpriseService.getDashboard()
            call.respond(dashboard)
        }

        route("/team") {
            get {
                val members = EnterpriseService.getTeamMembers()
                call.respond(members)
            }

            post {
                val request = call.receive<CreateTeamMemberRequest>()
                val member = EnterpriseService.addTeamMember(request.name, request.email, request.role)
                call.respond(HttpStatusCode.Created, member)
            }

            put("/{id}") {
                val memberId = call.parameters["id"]?.toLongOrNull()
                    ?: throw IllegalArgumentException("Invalid member ID")
                val request = call.receive<UpdateTeamMemberRequest>()
                val updated = EnterpriseService.updateTeamMember(memberId, request.role)
                call.respond(updated)
            }

            delete("/{id}") {
                val memberId = call.parameters["id"]?.toLongOrNull()
                    ?: throw IllegalArgumentException("Invalid member ID")
                EnterpriseService.removeTeamMember(memberId)
                call.respond(MessageResponse(message = "Team member removed"))
            }
        }

        get("/audit-log") {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 50
            val (entries, total) = EnterpriseService.getAuditLog(page, size)
            call.respond(PaginatedResponse(data = entries, page = page, size = size, total = total))
        }

        route("/security") {
            get {
                val settings = EnterpriseService.getSecuritySettings()
                call.respond(settings)
            }

            put {
                val request = call.receive<UpdateSecurityRequest>()
                val updated = EnterpriseService.updateSecuritySettings(
                    request.require2fa, request.passwordMinLength, request.sessionTimeoutMinutes,
                    request.dataRetention, request.autoLockTimeout
                )
                call.respond(updated)
            }
        }
    }
}
