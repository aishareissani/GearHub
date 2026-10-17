package org.umn.gearhub.data.dummy

object DummyLocations {
    const val DEFAULT = "Gading Serpong"
    val list: List<String> = DummyGears.list.map { it.location }.distinct()
}