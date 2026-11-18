package org.umn.gearhub.ui.screens.rent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.umn.gearhub.data.repository.BookingRepository
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.model.Gear
import org.umn.gearhub.util.DateUtils
import org.umn.gearhub.util.RentValidator

data class RentUiState(
    val gear: Gear?,
    val todayMillis: Long,
    val pickupMillis: Long? = null,
    val returnMillis: Long? = null,
    val phone: String = "",
    val pickupTouched: Boolean = false,
    val returnTouched: Boolean = false,
    val phoneTouched: Boolean = false,
    val submitted: Boolean = false,
    val isLoading: Boolean = false
) {
    val pickupError: String?
        get() = RentValidator.validatePickup(pickupMillis, todayMillis)

    val returnError: String?
        get() = RentValidator.validateReturn(pickupMillis, returnMillis) ?: availabilityError

    val phoneError: String?
        get() = RentValidator.validatePhone(phone)

    private val availabilityError: String?
        get() {
            val gearId = gear?.id ?: return null
            val pickup = pickupMillis ?: return null
            val returnDate = returnMillis ?: return null
            val start = DateUtils.toLocalDate(pickup)
            val end = DateUtils.toLocalDate(returnDate)
            return when {
                !BookingRepository.isWithinOwnerSchedule(gearId, start, end) ->
                    "Di luar tanggal tersedia"
                !BookingRepository.isReturnSelectable(gearId, start, end) ->
                    "Rentang tanggal bertabrakan dengan sewa lain"
                else -> null
            }
        }

    val isValid: Boolean
        get() = pickupError == null && returnError == null && phoneError == null

    val durationDays: Int?
        get() {
            val pickup = pickupMillis ?: return null
            val returnDate = returnMillis ?: return null
            val days = ((returnDate - pickup) / DateUtils.DAY_MILLIS).toInt()
            return if (days >= 1) days else null
        }

    val totalPrice
        get() = gear?.let { g ->
            durationDays?.let { days -> g.pricePerDay * days + SERVICE_FEE + DEPOSIT }
        }

    companion object {
        const val SERVICE_FEE = 5_000
        const val DEPOSIT = 100_000
    }
}

class RentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val gearId: Int = savedStateHandle.get<Int>("gearId") ?: -1

    private val _uiState = MutableStateFlow(
        RentUiState(
            gear = GearRepository.getById(gearId),
            todayMillis = DateUtils.todayUtcMillis()
        )
    )
    val uiState: StateFlow<RentUiState> = _uiState.asStateFlow()

    fun onPickupSelected(millis: Long) {
        _uiState.update { state ->
            val resetReturn = state.returnMillis?.let {
                it <= millis ||
                        (it - millis) / DateUtils.DAY_MILLIS > RentValidator.MAX_DAYS ||
                        !BookingRepository.isReturnSelectable(
                            gearId,
                            DateUtils.toLocalDate(millis),
                            DateUtils.toLocalDate(it)
                        )
            } ?: false
            state.copy(
                pickupMillis = millis,
                pickupTouched = true,
                returnMillis = if (resetReturn) null else state.returnMillis,
                returnTouched = if (resetReturn) false else state.returnTouched
            )
        }
    }

    fun onReturnSelected(millis: Long) {
        _uiState.update { it.copy(returnMillis = millis, returnTouched = true) }
    }

    fun onPhoneChange(value: String) {
        val digitsOnly = value.filter { it.isDigit() }.take(RentValidator.PHONE_MAX_LENGTH)
        _uiState.update { it.copy(phone = digitsOnly, phoneTouched = true) }
    }

    fun onSubmit() {
        _uiState.update { state ->
            if (state.isLoading) state
            else state.copy(submitted = true, isLoading = state.isValid)
        }
    }

    fun onLoadingFinished() {
        _uiState.update { it.copy(isLoading = false) }
    }
}