package com.tech.ssylix.alc4phase1challenge2.data.models

import androidx.annotation.Keep

@Keep
data class Deal(var destination: String?, var cost: String?, var accommodation: String?, var imageUrl: String?) {

    constructor() : this(null, null, null, null)
}