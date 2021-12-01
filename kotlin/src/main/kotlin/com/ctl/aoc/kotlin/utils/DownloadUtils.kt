package com.ctl.aoc.kotlin.utils

import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest.newBuilder
import java.net.http.HttpResponse

object DownloadUtils {

    fun downloadFile(url: String, cookies: Map<String, String>): InputStream {
        val httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build()

        val cookiesString = cookies.map { "${it.key}=${it.value}" }.joinToString(",")
        val get = newBuilder(URI(url))
                .header("cookie", cookiesString)
                .GET()
                .build()
        return httpClient.send(get, HttpResponse.BodyHandlers.ofInputStream())
                .body()
    }
}