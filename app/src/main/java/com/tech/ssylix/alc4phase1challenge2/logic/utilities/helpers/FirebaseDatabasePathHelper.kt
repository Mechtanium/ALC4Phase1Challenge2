package com.tech.ssylix.alc4phase1challenge2.logic.utilities.helpers

import com.tech.ssylix.alc4phase1challenge2.data.UserInfo

class FirebaseDatabasePathHelper(uid : String) {

    val sUserInfo = "Users/$uid"

    val isAdminPath = "$sUserInfo/${UserInfo.FIELD_TITLE_ADMIN}"

    val sDealList = "Deals"
}