package com.meloning.megaCoffee.common.extension

fun <T : Any> T?.isNull(): Boolean {
    return this == null
}

fun <T : Any> T?.isNotNull(): Boolean {
    return this != null
}
