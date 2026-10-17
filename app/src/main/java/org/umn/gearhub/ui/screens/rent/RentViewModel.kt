package org.umn.gearhub.ui.screens.rent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.umn.gearhub.data.dummy.DummyGears
import org.umn.gearhub.data.model.Gear
import org.umn.gearhub.util.DateUtils
import org.umn.gearhub.util.RentValidator

data class RentUiState(
    val gear: Gear?,
    val todayMillis: Long,
    val pickupMillis: Long? = null,
    val returnMillis: Long? = null,
    val pickupTouched: Boolean = false,
    val returnTouched: Boolean = false,
    val submitted: Boolean = false,
    val isLoading: Boolean = false
) {
    val pickupError: String?
        get() = RentValidator.validatePickup(pickupMillis, todayMillis)

    val returnError: String?
        get() = RentValidator.validateReturn(pickupMillis, returnMillis)

    val isValid: Boolean
        get() = pickupError == null && returnError == null

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
            gear = DummyGears.getById(gearId),
            todayMillis = DateUtils.todayUtcMillis()
        )
    )
    val uiState: StateFlow<RentUiState> = _uiState.asStateFlow()

    fun onPickupSelected(millis: Long) {
        _uiState.update { state ->
            val resetReturn = state.returnMillis?.let {
                it <= millis || (it - millis) / DateUtils.DAY_MILLIS > RentValidator.MAX_DAYS
            } ?: false
            state.copy(
                pickupMillis = millis,
                pickupTouched = true,
                returnMillis = if (resetReturn) null else state.returnMillis,
                returnTouched = state.returnTouched || resetReturn
            )
        }
    }

    fun onReturnSelected(millis: Long) {
        _uiState.update { it.copy(returnMillis = millis, returnTouched = true) }
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