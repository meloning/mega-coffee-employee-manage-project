package com.meloning.megaCoffee.filter

import java.util.*
import javax.servlet.http.HttpServletRequest

class TraceIds(
    private val mutableTraceIds: MutableList<String> = mutableListOf()
) {
    val traceIds: List<String> get() = mutableTraceIds.toList()

    init {
        mutableTraceIds.add(GCP_TRACE_ID)
        mutableTraceIds.add(AWS_ALB_TRACE_ID)
        mutableTraceIds.add(NGINX_TRACE_ID)
    }

    fun getTraceIdOrDefault(request: HttpServletRequest): String {
        return traceIds.firstNotNullOfOrNull(request::getHeader)
            ?: UUID.randomUUID().toString().replace("-", "")
    }

    companion object {
        private const val GCP_TRACE_ID = "x-cloud-trace-context"
        private const val AWS_ALB_TRACE_ID = "x-amzn-trace-id"
        private const val NGINX_TRACE_ID = "nginx-trace-id"
    }
}
