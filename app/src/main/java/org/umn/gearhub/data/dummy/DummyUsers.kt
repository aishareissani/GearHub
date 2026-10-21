package org.umn.gearhub.data.dummy

import org.umn.gearhub.data.model.User

object DummyUsers {
    private val users = mutableListOf(
        User("Jane Doe", "jane.doe@student.umn.ac.id", "password123"),
        User("William Parker", "william.parker@student.umn.ac.id", "password123"),
    )

    fun authenticate(email: String, password: String): User? =
        users.firstOrNull {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        }

    fun findByEmail(email: String): User? =
        users.firstOrNull { it.email.equals(email.trim(), ignoreCase = true) }

    fun emailExists(email: String): Boolean =
        users.any { it.email.equals(email.trim(), ignoreCase = true) }

    fun register(user: User) {
        users.add(user.copy(name = user.name.trim(), email = user.email.trim().lowercase()))
    }
}