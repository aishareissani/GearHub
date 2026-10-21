package org.umn.gearhub.util

import java.util.Locale

fun formatRupiah(amount: Int): String =
    "Rp" + String.format(Locale.forLanguageTag("id-ID"), "%,d", amount)