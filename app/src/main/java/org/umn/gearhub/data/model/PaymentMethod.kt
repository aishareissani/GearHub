package org.umn.gearhub.data.model

data class PaymentMethod(
    val id: Int,
    val name: String,
    val description: String,
    val isBalance: Boolean = false
)