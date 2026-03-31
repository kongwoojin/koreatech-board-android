package com.kongjak.koreatechboard.ui.notice

import java.util.UUID

sealed class NoticeSideEffect {
    data class GetAllNotices(val departments: List<String>) : NoticeSideEffect()
    data class UpdateRead(val uuid: UUID, val read: Boolean) : NoticeSideEffect()
    data class DeleteNotice(val uuid: UUID) : NoticeSideEffect()
    data class AddSelectedDepartment(val department: Int) : NoticeSideEffect()
    data class RemoveSelectedDepartment(val department: Int) : NoticeSideEffect()
}
