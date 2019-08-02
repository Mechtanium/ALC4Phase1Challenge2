package com.tech.ssylix.alc4phase1challenge2.data

import androidx.annotation.Keep

@Keep
data class UserInfo(var isAdmin : Boolean) {

    constructor() : this(false)

    companion object {
        const val FIELD_TITLE_ADMIN = "isAdmin"
    }
}