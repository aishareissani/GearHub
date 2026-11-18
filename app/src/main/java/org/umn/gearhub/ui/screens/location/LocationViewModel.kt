package org.umn.gearhub.ui.screens.location

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.umn.gearhub.data.model.GPSLocation
import org.umn.gearhub.util.getLocation

data class LocationUiState(
    val userLocation: GPSLocation? = null,
    val isUsingGPS: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val fixCount: Int = 0
)

class LocationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    fun detect(context: Context) {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, error = null) }
        getLocation(
            context = context.applicationContext,
            onSuccess = { location ->
                _uiState.update {
                    it.copy(
                        userLocation = location,
                        isLoading = false,
                        fixCount = it.fixCount + 1
                    )
                }
            },
            onError = { message ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = message,
                        fixCount = it.fixCount + 1
                    )
                }
            }
        )
    }

    fun onPermissionDenied() {
        _uiState.update {
            it.copy(
                isLoading = false,
                error = "Izin lokasi ditolak",
                fixCount = it.fixCount + 1
            )
        }
    }

    fun setUsingGPS(value: Boolean) {
        _uiState.update { it.copy(isUsingGPS = value) }
    }
}