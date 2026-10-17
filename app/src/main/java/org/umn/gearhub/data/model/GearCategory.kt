package org.umn.gearhub.data.model

import androidx.annotation.DrawableRes
import org.umn.gearhub.R

enum class GearCategory(val label: String, @DrawableRes val imageRes: Int) {
    KAMERA("Kamera", R.drawable.cat_kamera),
    LENSA("Lensa", R.drawable.cat_lensa),
    AUDIO("Audio", R.drawable.cat_audio),
    LIGHTING("Lighting", R.drawable.cat_lighting)
}