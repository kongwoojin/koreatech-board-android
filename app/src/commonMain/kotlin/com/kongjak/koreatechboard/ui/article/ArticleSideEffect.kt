package com.kongjak.koreatechboard.ui.article

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class ArticleSideEffect {
    data class FetchData(val department: String, val uuid: Uuid) : ArticleSideEffect()
}
