package org.umn.gearhub.data.dummy

import org.umn.gearhub.data.repository.GearRepository

object DummyLocations {
    const val DEFAULT = "Gading Serpong"
    val list: List<String> get() = GearRepository.list.map { it.location }.distinct()
}