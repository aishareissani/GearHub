package org.umn.gearhub.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SEARCH = "search"
    const val ADD_GEAR = "add_gear"
    const val ACTIVITY = "activity"
    const val PROFILE = "profile"
    const val SEARCH_PATTERN = "search?category={category}"
    const val DETAIL = "detail/{gearId}"
    const val RENT = "rent/{gearId}"
    const val PAYMENT = "payment/{gearId}"
    const val KTM = "ktm/{gearId}"

    fun search(category: String? = null) =
        if (category == null) SEARCH else "search?category=$category"
    fun detail(gearId: Int) = "detail/$gearId"
    fun rent(gearId: Int) = "rent/$gearId"
    fun payment(gearId: Int) = "payment/$gearId"
    fun ktm(gearId: Int) = "ktm/$gearId"
}