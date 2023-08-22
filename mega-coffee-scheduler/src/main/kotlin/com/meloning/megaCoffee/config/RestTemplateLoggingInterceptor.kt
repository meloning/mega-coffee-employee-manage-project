package com.meloning.megaCoffee.config

import com.meloning.megaCoffee.common.constant.Constant.NEW_LINE
import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class RestTemplateLoggingInterceptor : ClientHttpRequestInterceptor {
    private val log = LoggerFactory.getLogger(RestTemplateLoggingInterceptor::class.java)

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        loggingRequest(request, body)
        val response = execution.execute(request, body)
        loggingResponse(response)
        return response
    }

    private fun loggingRequest(request: HttpRequest, body: ByteArray) {
        val stringBuffer = StringBuffer()
        stringBuffer.append(NEW_LINE)
        stringBuffer.append("==============================[Request]==============================").append(NEW_LINE)
        stringBuffer.append("Headers : " + request.headers).append(NEW_LINE)
        stringBuffer.append("Request Method : " + request.method).append(NEW_LINE)
        stringBuffer.append("Request URI : " + request.uri).append(NEW_LINE)
        stringBuffer.append(
            "Request body : " +
                if (body.isEmpty()) null
                else String(body, StandardCharsets.UTF_8)
        ).append(NEW_LINE)
        log.debug(stringBuffer.toString())
    }

    private fun loggingResponse(response: ClientHttpResponse) {
        val body = getBody(response)
        val stringBuffer = StringBuffer()
        stringBuffer.append(NEW_LINE)
        stringBuffer.append("==============================[Response]==============================").append(NEW_LINE)
        stringBuffer.append("Headers : " + response.headers).append(NEW_LINE)
        stringBuffer.append("Response Status : " + response.rawStatusCode).append(NEW_LINE)
        stringBuffer.append("Response body : $body").append(NEW_LINE)
        log.debug(stringBuffer.toString())
    }

    private fun getBody(response: ClientHttpResponse): String {
        try {
            BufferedReader(InputStreamReader(response.body)).use { br ->
                val stringBuffer = StringBuffer()
                br.lines().forEach { str: String? -> stringBuffer.append(str) }
                return stringBuffer.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException(e.localizedMessage)
        }
    }
}
