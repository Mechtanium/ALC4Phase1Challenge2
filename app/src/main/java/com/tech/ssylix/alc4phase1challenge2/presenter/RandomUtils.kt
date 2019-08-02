package com.tech.ssylix.alc4phase1challenge2.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.nio.charset.Charset
import java.util.*

fun Context.toast(obj: Any = "Here", duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, obj.toString(), duration).show()
}

fun <T> T.debugLog(logLevel: Int = Log.ERROR): T {
    when (logLevel) {
        Log.VERBOSE -> {
            Log.v("Debugger", this.toString())
        }
        Log.DEBUG -> {
            Log.d("Debugger", this.toString())
        }
        Log.INFO -> {
            Log.i("Debugger", this.toString())
        }
        Log.WARN -> {
            Log.w("Debugger", this.toString())
        }
        Log.ERROR -> {
            Log.e("Debugger", this.toString())
        }
        Log.ASSERT -> {
            Log.wtf("Debugger", this.toString())
        }
    }
    return this
}

fun generateRandomKey(length : Int) : String {
    var n = length
    // length is bounded by 256 Character
    val array = ByteArray(256)
    Random().nextBytes(array)

    val randomString = String(array, Charset.forName("UTF-8"))

    // Create a StringBuffer to store the result
    val r = StringBuffer()

    // Append first 20 alphanumeric characters
    // from the generated random String into the result
    for (k in 0 until randomString.length) {
        val ch = randomString[k]
        if ((ch in 'a'..'z' || ch in 'A'..'Z' || ch in '0'..'9') && n > 0) {
            r.append(ch)
            n--
        }
    }
    // return the resultant string
    return r.toString()
}