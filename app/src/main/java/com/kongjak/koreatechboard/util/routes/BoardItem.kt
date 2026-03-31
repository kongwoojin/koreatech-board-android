package com.kongjak.koreatechboard.util.routes

import com.kongjak.koreatechboard.R

sealed class BoardItem(
    val stringResource: Int,
    val painterResource: Int,
    val board: String
) {
    object Notice : BoardItem(R.string.board_notice, R.drawable.ic_bottom_navigation_notice, "notice")
    object Free :
        BoardItem(R.string.board_free_board, R.drawable.ic_bottom_navigation_freeboard, "free")
    object Job : BoardItem(R.string.board_job_board, R.drawable.ic_bottom_navigation_jobboard, "job")
    object Lecture : BoardItem(R.string.board_lecture_board, R.drawable.ic_bottom_navigation_lecture, "lecture")
    object Scholar : BoardItem(R.string.board_scholar_board, R.drawable.ic_bottom_navigation_scholar, "scholar")
    object Bachelor : BoardItem(R.string.board_bachelor_board, R.drawable.ic_bottom_navigation_bachelor, "bachelor")
    object PDS : BoardItem(R.string.board_pds_board, R.drawable.ic_bottom_navigation_pds, "pds")

    companion object {
        fun valueOf(name: String): BoardItem {
            return when (name.lowercase()) {
                Notice.board -> Notice
                Free.board -> Free
                Job.board -> Job
                Lecture.board -> Lecture
                Scholar.board -> Scholar
                Bachelor.board -> Bachelor
                PDS.board -> PDS
                else -> Notice
            }
        }
    }
}

val cseBoards = listOf(
    BoardItem.Notice,
    BoardItem.Lecture,
    BoardItem.Job,
    BoardItem.PDS
)

val archBoards = listOf(
    BoardItem.Notice,
    BoardItem.Job,
    BoardItem.Free
)

val ideBoards = listOf(
    BoardItem.Notice,
    BoardItem.Job
)

val mechanicalBoards = listOf(
    BoardItem.Notice,
    BoardItem.Lecture,
    BoardItem.Bachelor,
    BoardItem.Job
)

val mechatronicsBoards = listOf(
    BoardItem.Notice,
    BoardItem.Bachelor,
    BoardItem.Job,
    BoardItem.PDS,
    BoardItem.Free
)

val schoolBoards = listOf(
    BoardItem.Notice,
    BoardItem.Scholar,
    BoardItem.Bachelor
)

val dormBoards = listOf(
    BoardItem.Notice
)

val iteBoards = listOf(
    BoardItem.Notice,
    BoardItem.Job
)

val simBoards = listOf(
    BoardItem.Notice
)

val mseBoards = listOf(
    BoardItem.Notice,
    BoardItem.Bachelor,
    BoardItem.PDS
)

val aceBoards = listOf(
    BoardItem.Notice,
    BoardItem.Bachelor,
    BoardItem.Job,
    BoardItem.PDS
)
