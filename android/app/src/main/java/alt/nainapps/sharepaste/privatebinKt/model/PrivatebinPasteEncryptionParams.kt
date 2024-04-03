package alt.nainapps.sharepaste.privatebinKt.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import java.security.SecureRandom
import java.util.Base64

// Encryption Params used with Privatebin(1.3+) paste format v2
@Serializable(with = PrivatebinPasteEncryptionParamsSerializer::class)
data class PrivatebinPasteEncryptionParams(
    val kdfIterations: Int = 100000, val kdfKeysize: Int = 256,
    val cipherTagSize: Int = 128, val cipherAlgo: String = "aes", val cipherMode: String = "gcm",
    val compressionType: CompressionType = CompressionType.ZLIB,
    val suggestCipherIV: ByteArray? = null, val suggestKdfSalt: ByteArray? = null
) {
    // If suggestCipherIV is null, generate a new one; otherwise, use the provided value
    val cipherIV: ByteArray = suggestCipherIV ?: ByteArray(16).also { SecureRandom().nextBytes(it) }
    // If suggestKdfSalt is null, generate a new one; otherwise, use the provided value
    val kdfSalt: ByteArray = suggestKdfSalt ?: ByteArray(8).also { SecureRandom().nextBytes(it) }

    fun toEncryptionParamsList(): List<Any> {
        return listOf(
            Base64.getEncoder().encodeToString(cipherIV),
            Base64.getEncoder().encodeToString(kdfSalt),
            kdfIterations,
            kdfKeysize,
            cipherTagSize,
            cipherAlgo,
            cipherMode,
            compressionType.name.lowercase()
        )
    }

    companion object {
        fun fromPasteMetaJson(jsonString: String): PrivatebinPasteEncryptionParams {
            val plainPasteAAData = PlainPasteAAData.fromPasteMetaJson(jsonString)
            return plainPasteAAData.encryptionParams
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PrivatebinPasteEncryptionParams

        if (kdfIterations != other.kdfIterations) return false
        if (kdfKeysize != other.kdfKeysize) return false
        if (cipherTagSize != other.cipherTagSize) return false
        if (cipherAlgo != other.cipherAlgo) return false
        if (cipherMode != other.cipherMode) return false
        if (compressionType != other.compressionType) return false
        if (!cipherIV.contentEquals(other.cipherIV)) return false
        if (!kdfSalt.contentEquals(other.kdfSalt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kdfIterations
        result = 31 * result + kdfKeysize
        result = 31 * result + cipherTagSize
        result = 31 * result + cipherAlgo.hashCode()
        result = 31 * result + cipherMode.hashCode()
        result = 31 * result + compressionType.hashCode()
        result = 31 * result + cipherIV.contentHashCode()
        result = 31 * result + kdfSalt.contentHashCode()
        return result
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PrivatebinPasteEncryptionParams::class)
object PrivatebinPasteEncryptionParamsSerializer : KSerializer<PrivatebinPasteEncryptionParams> {
    override fun serialize(encoder: Encoder, value: PrivatebinPasteEncryptionParams) {
        val jsonArray = JsonArray(value.toEncryptionParamsList().map {
            when (it) {
                is Int -> JsonPrimitive(it)
                is String -> JsonPrimitive(it)
                else -> throw IllegalArgumentException("Unsupported type for serialization")
            }
        })
        encoder.encodeSerializableValue(JsonElement.serializer(), jsonArray)
    }

    override fun deserialize(decoder: Decoder): PrivatebinPasteEncryptionParams {
        val jsonArray = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonArray
        val values = jsonArray.map { it.jsonPrimitive.content }

        val cipherIV = Base64.getDecoder().decode(values[0])
        val kdfSalt = Base64.getDecoder().decode(values[1])
        val kdfIterations = values[2].toInt()
        val kdfKeysize = values[3].toInt()
        val cipherTagSize = values[4].toInt()
        val cipherAlgo = values[5]
        val cipherMode = values[6]
        val compressionType = CompressionType.valueOf(values[7].uppercase())

        return PrivatebinPasteEncryptionParams(
            kdfIterations = kdfIterations,
            kdfKeysize = kdfKeysize,
            cipherTagSize = cipherTagSize,
            cipherAlgo = cipherAlgo,
            cipherMode = cipherMode,
            compressionType = compressionType,
            suggestCipherIV = cipherIV,
            suggestKdfSalt = kdfSalt
        )
    }
}