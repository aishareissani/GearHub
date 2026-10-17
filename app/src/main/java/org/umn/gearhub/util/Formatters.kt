package org.umn.gearhub.util

import java.util.Locale

fun formatRupiah(amount: Int): String =
    "Rp" + String.format(Locale("id", "ID"), "%,d", amount)