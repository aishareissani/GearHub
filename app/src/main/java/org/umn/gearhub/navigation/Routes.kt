package org.umn.gearhub.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SEARCH = "search"
    const val LOCATION = "location"
    const val SEARCH_PATTERN = "search?category={category}"
    const val DETAIL = "detail/{gearId}"
    const val RENT = "rent/{gearId}"
    const val PAYMENT = "payment/{gearId}/{pickup}/{returnDate}"
    const val SUCCESS = "success/{gearId}/{pickup}/{returnDate}/{methodId}"

    fun search(category: String? = null) =
        if (category == null) SEARCH else "search?category=$category"
    fun detail(gearId: Int) = "detail/$gearId"
    fun rent(gearId: Int) = "rent/$gearId"
    fun payment(gearId: Int, pickup: Long, returnDate: Long) =
        "payment/$gearId/$pickup/$returnDate"
    fun success(gearId: Int, pickup: Long, returnDate: Long, methodId: Int) =
        "success/$gearId/$pickup/$returnDate/$methodId"
}