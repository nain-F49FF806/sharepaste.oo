package alt.nainapps.sharepaste.utils

import android.util.Log
import java.io.InputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val TAG = "Utils"

// https://stackoverflow.com/a/68822715
fun bytesToHumanReadableSize(bytes: Long) = when {
    bytes >= 1 shl 30 -> "%.1f GB".format(bytes.toDouble() / (1 shl 30))
    bytes >= 1 shl 20 -> "%.1f MB".format(bytes.toDouble() / (1 shl 20))
    bytes >= 1 shl 10 -> "%.0f kB".format(bytes.toDouble() / (1 shl 10))
    else -> "$bytes bytes"
}

@OptIn(ExperimentalEncodingApi::class)
fun inputStreamToBase64String(inputStream: InputStream): String? = try {
    Base64.encode(inputStream.readBytes())
} catch (e: Exception) {
    Log.e(TAG, "Encoding Base64 Error: ${e.message}")
    null // Handle exception appropriately
}
