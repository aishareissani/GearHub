package org.umn.gearhub.navigation

object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val DETAIL = "detail/{gearId}"
    const val RENT = "rent/{gearId}"
    const val KTM = "ktm/{gearId}"

    fun detail(gearId: Int) = "detail/$gearId"
    fun rent(gearId: Int) = "rent/$gearId"
    fun ktm(gearId: Int) = "ktm/$gearId"
}