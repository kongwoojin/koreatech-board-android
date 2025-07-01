package com.kongjak.koreatechboard.util

import com.kongjak.koreatechboard.BuildConfig

fun isDebug() = BuildConfig.DEBUG
fun isRelease() = !isDebug()
