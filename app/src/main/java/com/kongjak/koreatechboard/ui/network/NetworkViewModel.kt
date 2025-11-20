package com.kongjak.koreatechboard.ui.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kongjak.koreatechboard.util.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val networkUtil: NetworkUtil
) : ContainerHost<NetworkState, NetworkSideEffect>, ViewModel() {

    override val container = container<NetworkState, NetworkSideEffect>(NetworkState())

    init {
        getNetworkState()
    }

    private fun getNetworkState() {
        viewModelScope.launch {
            networkUtil.networkState().collectLatest { networkConnected ->
                intent {
                    if (networkConnected) {
                        postSideEffect(NetworkSideEffect.Connected)
                    } else {
                        postSideEffect(NetworkSideEffect.Disconnected)
                    }
                }
            }
        }
    }

    fun handleSideEffect(sideEffect: NetworkSideEffect) {
        when (sideEffect) {
            is NetworkSideEffect.Connected -> {
                intent {
                    reduce {
                        state.copy(isConnected = true)
                    }
                }
            }

            is NetworkSideEffect.Disconnected -> {
                intent {
                    reduce {
                        state.copy(isConnected = false)
                    }
                }
            }
        }
    }
}
