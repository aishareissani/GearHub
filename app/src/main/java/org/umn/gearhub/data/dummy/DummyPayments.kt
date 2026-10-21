package org.umn.gearhub.data.dummy

import org.umn.gearhub.data.model.PaymentMethod

object DummyPayments {
    const val BALANCE = 40_000
    const val DEFAULT_ID = 2

    val list = listOf(
        PaymentMethod(1, "Saldo GearHub", "Rp40.000", isBalance = true),
        PaymentMethod(2, "QRIS", "Gopay / OVO / Dana"),
        PaymentMethod(3, "Virtual Account", "BCA / Mandiri / BRI")
    )

    fun getById(id: Int): PaymentMethod? = list.find { it.id == id }
}