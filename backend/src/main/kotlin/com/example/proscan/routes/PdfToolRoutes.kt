package com.example.proscan.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import java.io.ByteArrayOutputStream
import java.io.File

fun Route.pdfToolRoutes() {
    route("/pdf") {
        post("/merge") {
            val multipart = call.receiveMultipart()
            val tempFiles = mutableListOf<File>()

            try {
                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val tempFile = File.createTempFile("merge_", ".pdf")
                        tempFile.writeBytes(part.provider().toByteArray())
                        tempFiles.add(tempFile)
                    }
                    part.dispose()
                }

                if (tempFiles.size < 2) throw IllegalArgumentException("At least 2 PDF files required for merge")

                val merger = PDFMergerUtility()
                val output = ByteArrayOutputStream()
                merger.destinationStream = output

                tempFiles.forEach { file ->
                    merger.addSource(file)
                }
                merger.mergeDocuments(null)

                call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"merged.pdf\"")
                call.respondBytes(output.toByteArray(), ContentType.Application.Pdf)
            } finally {
                tempFiles.forEach { it.delete() }
            }
        }

        post("/compress") {
            val multipart = call.receiveMultipart()
            var fileBytes: ByteArray? = null

            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    fileBytes = part.provider().toByteArray()
                }
                part.dispose()
            }

            val bytes = fileBytes ?: throw IllegalArgumentException("No PDF file uploaded")

            // Basic "compression" — re-save the document which can reduce size
            val document = Loader.loadPDF(bytes)
            val output = ByteArrayOutputStream()
            document.save(output)
            document.close()

            call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"compressed.pdf\"")
            call.respondBytes(output.toByteArray(), ContentType.Application.Pdf)
        }

        post("/watermark") {
            val multipart = call.receiveMultipart()
            var fileBytes: ByteArray? = null
            var watermarkText = "ProScan"

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> fileBytes = part.provider().toByteArray()
                    is PartData.FormItem -> {
                        if (part.name == "text") watermarkText = part.value
                    }
                    else -> {}
                }
                part.dispose()
            }

            val bytes = fileBytes ?: throw IllegalArgumentException("No PDF file uploaded")
            val document = Loader.loadPDF(bytes)
            val font = PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)

            for (page in document.pages) {
                val contentStream = PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)
                contentStream.setFont(font, 48f)
                contentStream.setNonStrokingColor(0.78f, 0.78f, 0.78f) // Light gray
                contentStream.beginText()

                val pageWidth = page.mediaBox.width
                val pageHeight = page.mediaBox.height
                contentStream.newLineAtOffset(pageWidth / 4, pageHeight / 3)
                contentStream.showText(watermarkText)
                contentStream.endText()
                contentStream.close()
            }

            val output = ByteArrayOutputStream()
            document.save(output)
            document.close()

            call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"watermarked.pdf\"")
            call.respondBytes(output.toByteArray(), ContentType.Application.Pdf)
        }
    }
}
