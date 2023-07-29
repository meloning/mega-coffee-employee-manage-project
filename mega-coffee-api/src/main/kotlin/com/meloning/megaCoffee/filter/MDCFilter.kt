package com.meloning.megaCoffee.filter

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class MDCFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val traceId = TraceIds().getTraceIdOrDefault(request)
        MDC.put(RESPONSE_TRACE_NAME, traceId)
        response.addHeader(RESPONSE_TRACE_NAME, traceId)

        filterChain.doFilter(request, response)
        MDC.clear()
    }

    companion object {
        const val RESPONSE_TRACE_NAME = "x-meloning-trace-id"
    }
}
