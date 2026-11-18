package org.umn.gearhub.ui.screens.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.umn.gearhub.data.dummy.DummyPayments
import org.umn.gearhub.data.model.Gear
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.repository.RentalRepository
import org.umn.gearhub.util.DateUtils
import org.umn.gearhub.util.PriceCalculator

data class PaymentUiState(
    val gear: Gear?,
    val pickupMillis: Long,
    val returnMillis: Long,
    val selectedMethodId: Int = DummyPayments.DEFAULT_ID,
    val isLoading: Boolean = false,
    val isPaid: Boolean = false,
    val errorMessage: String? = null
) {
    val totalPrice: Int
        get() = gear?.let { PriceCalculator.total(it.pricePerDay, pickupMillis, returnMillis) } ?: 0

    fun isMethodEnabled(methodId: Int): Boolean {
        val method = DummyPayments.getById(methodId) ?: return false
        return !method.isBalance || DummyPayments.BALANCE >= totalPrice
    }

    val canPay: Boolean
        get() = gear != null && isMethodEnabled(selectedMethodId) && !isLoading
}

class PaymentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val gearId: Int = savedStateHandle.get<Int>("gearId") ?: -1
    private val pickup: Long = savedStateHandle.get<Long>("pickup") ?: 0L
    private val returnDate: Long = savedStateHandle.get<Long>("returnDate") ?: 0L

    private val _uiState = MutableStateFlow(
        PaymentUiState(
            gear = GearRepository.getById(gearId),
            pickupMillis = pickup,
            returnMillis = returnDate
        )
    )
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun onMethodSelected(methodId: Int) {
        _uiState.update { state ->
            if (state.isLoading || !state.isMethodEnabled(methodId)) state
            else state.copy(selectedMethodId = methodId, errorMessage = null)
        }
    }

    fun onPay() {
        val state = _uiState.value
        if (!state.canPay || state.isPaid) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = runCatching {
                RentalRepository.create(
                    gearId = gearId,
                    pickup = DateUtils.toLocalDate(pickup),
                    returnDate = DateUtils.toLocalDate(returnDate),
                    methodId = state.selectedMethodId
                )
            }
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, isPaid = true)
                else it.copy(isLoading = false, errorMessage = "Pembayaran gagal. Tanggal mungkin sudah dibooking, coba lagi.")
            }
        }
    }
}