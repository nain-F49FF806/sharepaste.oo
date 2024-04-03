package alt.nainapps.sharepaste.privatebinKt.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PlainPasteData(
    val paste: String,
    val attachment: String? = null, // [data URI as per RFC 2397]
    val attachmentName: String? = null,
    val children: List<String>? = null
) {
    fun toPasteDataJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromPasteDataJson(json: String): PlainPasteData {
            return Json.decodeFromString(json)
        }
    }
}