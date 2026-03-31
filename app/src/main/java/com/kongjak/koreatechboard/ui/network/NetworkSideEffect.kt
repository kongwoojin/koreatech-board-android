package com.kongjak.koreatechboard.ui.network

sealed class NetworkSideEffect {
    object Connected : NetworkSideEffect()
    object Disconnected : NetworkSideEffect()
}
