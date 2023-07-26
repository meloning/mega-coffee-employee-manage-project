package com.meloning.megaCoffee.infra.database.mysql.util

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberExpression

/**
 * Primitive Type을 NumberExpression으로 변환하는 확장함수
 * querydsl-kotlin-codegen -> 5.0.0
 *
 * @see https://github.com/querydsl/querydsl/pull/3346
 */
inline fun <reified T : Number> T.asNumberExpression(): NumberExpression<*> =
    Expressions.asNumber(
        when (this) {
            is Double -> this.toDouble()
            is Float -> this.toFloat()
            is Long -> this.toLong()
            is Int -> this.toInt()
            is Short -> this.toShort()
            is Byte -> this.toByte()
            else -> throw IllegalStateException("Unsupported type")
        }
    )
