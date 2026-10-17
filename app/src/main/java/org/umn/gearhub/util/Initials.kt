package org.umn.gearhub.util

fun initialsOf(name: String): String =
    name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }