package alt.nainapps.sharepaste.privatebinKt.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PrivatebinPasteV2(
    val adata: PlainPasteAAData,
    val ct: String, //  cipher_text as base64 encoded string.
    val meta: PrivatebinPasteMetaData,
    val v: Int,
    // other server provided fields
    val status: Int? = null,
    val id: String? = null,
    val deleteToken: String? = null,
    val url: String? = null,
    val comments: List<PrivatebinPasteV2>? = null,
    val comment_count: Int? = null,
    val comment_offset: Int? = null,
    // comment specific fields
    val pasteid: String? = null,
    val parentid: String? = null,


    ) {
    init {
        assert(v == 2) // version should be 2
    }

    fun toPrivatebinPasteJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        private val jsonParser = Json { ignoreUnknownKeys = true }

        fun fromPrivatebinPasteJson(json: String): PrivatebinPasteV2 {
            return jsonParser.decodeFromString(json)
        }

    }
}
