package com.example.proscan.service

import com.example.proscan.database.tables.PaymentMethods
import com.example.proscan.database.tables.Transactions
import com.example.proscan.models.responses.PaymentMethodResponse
import com.example.proscan.models.responses.TransactionResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object PaymentService {

    fun getMethods(userId: Long): List<PaymentMethodResponse> {
        return transaction {
            PaymentMethods.selectAll().where { PaymentMethods.userId eq userId }
                .orderBy(PaymentMethods.id)
                .map { row ->
                    PaymentMethodResponse(
                        id = row[PaymentMethods.id],
                        name = row[PaymentMethods.name],
                        icon = row[PaymentMethods.icon],
                        lastFour = row[PaymentMethods.lastFour],
                        isDefault = row[PaymentMethods.isDefault]
                    )
                }
        }
    }

    fun addMethod(userId: Long, name: String, icon: String, lastFour: String): PaymentMethodResponse {
        return transaction {
            val id = PaymentMethods.insert {
                it[PaymentMethods.userId] = userId
                it[PaymentMethods.name] = name
                it[PaymentMethods.icon] = icon
                it[PaymentMethods.lastFour] = lastFour
                it[PaymentMethods.isDefault] = false
            }[PaymentMethods.id]

            PaymentMethodResponse(id, name, icon, lastFour, false)
        }
    }

    fun setDefault(userId: Long, methodId: Long): PaymentMethodResponse {
        return transaction {
            // Unset all defaults for this user
            PaymentMethods.update({ PaymentMethods.userId eq userId }) {
                it[isDefault] = false
            }
            // Set the chosen one
            PaymentMethods.update({
                (PaymentMethods.id eq methodId) and (PaymentMethods.userId eq userId)
            }) {
                it[isDefault] = true
            }

            PaymentMethods.selectAll().where {
                (PaymentMethods.id eq methodId) and (PaymentMethods.userId eq userId)
            }.firstOrNull()?.let { row ->
                PaymentMethodResponse(
                    row[PaymentMethods.id], row[PaymentMethods.name], row[PaymentMethods.icon],
                    row[PaymentMethods.lastFour], row[PaymentMethods.isDefault]
                )
            } ?: throw NoSuchElementException("Payment method not found")
        }
    }

    fun deleteMethod(userId: Long, methodId: Long) {
        transaction {
            val count = PaymentMethods.deleteWhere {
                (PaymentMethods.id eq methodId) and (PaymentMethods.userId eq userId)
            }
            if (count == 0) throw NoSuchElementException("Payment method not found")
        }
    }

    fun getHistory(userId: Long): List<TransactionResponse> {
        return transaction {
            Transactions.selectAll().where { Transactions.userId eq userId }
                .orderBy(Transactions.id, SortOrder.DESC)
                .map { row ->
                    TransactionResponse(
                        id = row[Transactions.id],
                        description = row[Transactions.description],
                        amount = row[Transactions.amount],
                        date = row[Transactions.date],
                        status = row[Transactions.status]
                    )
                }
        }
    }
}
