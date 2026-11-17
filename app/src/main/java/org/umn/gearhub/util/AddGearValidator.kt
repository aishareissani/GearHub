package org.umn.gearhub.util

object AddGearValidator {
    const val MIN_PRICE = 10_000
    const val MAX_PRICE = 1_000_000

    fun validateName(value: String): String? {
        val name = value.trim()
        return when {
            name.isEmpty() -> "Nama gear wajib diisi"
            name.length < 3 -> "Nama gear minimal 3 karakter"
            else -> null
        }
    }

    fun validateCategory(value: String?): String? =
        if (value == null) "Kategori wajib dipilih" else null

    fun validatePrice(value: String): String? {
        val price = value.toIntOrNull()
        return when {
            value.isEmpty() -> "Harga sewa wajib diisi"
            !value.all { it.isDigit() } -> "Harga hanya boleh berisi angka"
            price == null || price !in MIN_PRICE..MAX_PRICE ->
                "Harga antara ${formatRupiah(MIN_PRICE)} sampai ${formatRupiah(MAX_PRICE)}"
            else -> null
        }
    }

    fun validateLocation(value: String?): String? =
        if (value == null) "Lokasi pengambilan wajib dipilih" else null

    fun validateDescription(value: String): String? {
        val text = value.trim()
        return when {
            text.isEmpty() -> "Deskripsi wajib diisi"
            text.length < 10 -> "Deskripsi minimal 10 karakter"
            else -> null
        }
    }

    fun validateIncluded(value: String): String? =
        if (splitItems(value).isEmpty()) "Isi minimal satu kelengkapan" else null

    fun splitItems(value: String): List<String> =
        value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
