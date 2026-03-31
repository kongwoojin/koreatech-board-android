package com.kongjak.koreatechboard.ui.article

import java.util.UUID

sealed class ArticleSideEffect {
    data class FetchData(val department: String, val uuid: UUID) : ArticleSideEffect()
}
