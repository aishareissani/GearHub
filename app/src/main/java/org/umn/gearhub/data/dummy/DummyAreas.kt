package org.umn.gearhub.data.dummy

object DummyAreas {
    private val coordinates: Map<String, Pair<Double, Double>> = mapOf(
        "BSD City" to Pair(-6.306032, 106.642848),
        "Gading Serpong" to Pair(-6.242492922471885, 106.6203480831786),
        "Kelapa Dua" to Pair(-6.2395155, 106.6114474),
        "Medang" to Pair(-6.265696, 106.614639),
        "Pagedangan" to Pair(-6.29525, 106.629003),
        "Karawaci" to Pair(-6.20376, 106.630216),
        "Tangerang Kota" to Pair(-6.178306, 106.631889),
        "Pondok Aren" to Pair(-6.2667, 106.7097),
        "Alam Sutera" to Pair(-6.24306, 106.65222)
    )

    val names: List<String> get() = coordinates.keys.toList()
    fun coordinateOf(areaName: String): Pair<Double, Double>? = coordinates[areaName]
}