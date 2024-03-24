package com.kongjak.koreatechboard.util

import android.text.util.Linkify
import android.widget.TextView
import com.kongjak.koreatechboard.domain.model.Files
import java.util.regex.Pattern

var TextView.fileText: ArrayList<Files>?
    get() {
        return null
    }
    set(value) {
        if (value != null) {
            for (file in value) {
                append(file.fileName)
                if (value.iterator().hasNext()) {
                    append("\n")
                }
            }
            val transform = Linkify.TransformFilter { _, _ -> "" }

            for (file in value) {
                val pattern =
                    Pattern.compile(file.fileName.replace("(", "\\(").replace(")", "\\)"))
                Linkify.addLinks(this, pattern, file.fileUrl, null, transform)
            }
        }
    }
