package org.umn.gearhub.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import org.umn.gearhub.data.model.Gear
import org.umn.gearhub.data.remote.GearRow
import org.umn.gearhub.data.remote.supabase

object GearRepository {
    var list: List<Gear> by mutableStateOf(emptyList())
        private set

    fun getById(id: Int): Gear? = list.find { it.id == id }

    suspend fun load() {
        list = supabase.from("gears")
            .select { order("id", Order.ASCENDING) }
            .decodeList<GearRow>()
            .map(GearRow::toGear)
    }

    suspend fun add(gear: Gear): Gear {
        val saved = supabase.from("gears")
            .insert(GearRow.from(gear)) { select() }
            .decodeSingle<GearRow>()
            .toGear()
        list = list + saved
        return saved
    }
}
