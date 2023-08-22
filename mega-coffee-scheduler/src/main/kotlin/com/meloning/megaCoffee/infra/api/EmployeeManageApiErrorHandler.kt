package com.meloning.megaCoffee.infra.api

import com.meloning.megaCoffee.common.constant.Constant.NEW_LINE
import org.slf4j.LoggerFactory
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import java.io.BufferedReader
import java.io.InputStreamReader

class EmployeeManageApiErrorHandler : ResponseErrorHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun hasError(response: ClientHttpResponse): Boolean {
        return !response.statusCode.is2xxSuccessful
    }

    override fun handleError(response: ClientHttpResponse) {
        val error = getErrorAsString(response)
        logging(response, error)
    }

    private fun logging(response: ClientHttpResponse, error: String) {
        val stringBuffer = StringBuffer()
        stringBuffer.append(NEW_LINE)
        stringBuffer.append("==============================[Response]==============================").append(NEW_LINE)
        stringBuffer.append("Headers : " + response.headers).append(NEW_LINE)
        stringBuffer.append("Response Status : " + response.rawStatusCode).append(NEW_LINE)
        stringBuffer.append("Response body : $error").append(NEW_LINE)

        logger.warn(stringBuffer.toString())
    }

    private fun getErrorAsString(response: ClientHttpResponse): String {
        BufferedReader(InputStreamReader(response.body)).use { br ->
            val stringBuffer = StringBuffer()
            br.lines().forEach { str: String? -> stringBuffer.append(str) }
            return stringBuffer.toString()
        }
    }
}
