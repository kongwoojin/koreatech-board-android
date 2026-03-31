package com.kongjak.koreatechboard.ui.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kongjak.koreatechboard.util.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

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
