package alt.nainapps.sharepaste.privatebin.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Base64
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

/**
 * Additional Authenticated Data
 **/
@Serializable(with = PlainPasteAADataSerializer::class)
data class PlainPasteAAData (
    val encryptionParams: PrivatebinPasteEncryptionParams,
    val formatter: TextFormatter,
    val openDiscussion: Boolean,
    val burnAfterReading: Boolean
) {
    fun toPasteMetaJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromPasteMetaJson(jsonString: String): PlainPasteAAData {
            val pasteMeta = Json.decodeFromString<List<*>>(jsonString)

            // Extracting the parameters from the first list in the JSON
            val parametersList = pasteMeta[0] as List<*>
            val cipherIV = Base64.getDecoder().decode(parametersList[0] as String)
            val kdfSalt = Base64.getDecoder().decode(parametersList[1] as String)
            val kdfIterations = parametersList[2] as Int
            val kdfKeysize = parametersList[3] as Int
            val cipherTagSize = parametersList[4] as Int
            val cipherAlgo = parametersList[5] as String
            val cipherMode = parametersList[6] as String
            val compressionType = CompressionType.valueOf((parametersList[7] as String).uppercase())
            val encryptionParams = PrivatebinPasteEncryptionParams(
                kdfIterations = kdfIterations,
                kdfKeysize = kdfKeysize,
                cipherTagSize = cipherTagSize,
                cipherAlgo = cipherAlgo,
                cipherMode = cipherMode,
                compressionType = compressionType,
                suggestCipherIV = cipherIV,
                suggestKdfSalt = kdfSalt,
            )
            // Extracting formatter type from string
            val formatterString = pasteMeta[1] as String
            val formatter = TextFormatter.valueOf(formatterString.uppercase())
            // Extracting options as boolean values
            val openDiscussion = pasteMeta[2] as Boolean
            val burnAfterReading = pasteMeta[3] as Boolean
            return PlainPasteAAData(
                encryptionParams = encryptionParams,
                formatter = formatter,
                openDiscussion = openDiscussion,
                burnAfterReading = burnAfterReading
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PlainPasteAAData::class)
object PlainPasteAADataSerializer : KSerializer<PlainPasteAAData> {
    override fun serialize(encoder: Encoder, value: PlainPasteAAData) {
        val jsonArray = JsonArray(listOf(
            JsonArray(listOf(
                JsonPrimitive(Base64.getEncoder().encodeToString(value.encryptionParams.cipherIV)),
                JsonPrimitive(Base64.getEncoder().encodeToString(value.encryptionParams.kdfSalt)),
                JsonPrimitive(value.encryptionParams.kdfIterations),
                JsonPrimitive(value.encryptionParams.kdfKeysize),
                JsonPrimitive(value.encryptionParams.cipherTagSize),
                JsonPrimitive(value.encryptionParams.cipherAlgo),
                JsonPrimitive(value.encryptionParams.cipherMode),
                JsonPrimitive(value.encryptionParams.compressionType.name.lowercase())
            )),
            JsonPrimitive(value.formatter.name.lowercase()),
            JsonPrimitive(if (value.openDiscussion) 1 else 0),
            JsonPrimitive(if (value.burnAfterReading) 1 else 0)
        ))
        encoder.encodeSerializableValue(JsonElement.serializer(), jsonArray)
    }
    override fun deserialize(decoder: Decoder): PlainPasteAAData {
        val jsonArray = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonArray
        val encryptionParamsArray = jsonArray[0] as JsonArray
        val encryptionParams = PrivatebinPasteEncryptionParams(
            suggestCipherIV = Base64.getDecoder().decode(encryptionParamsArray[0].jsonPrimitive.content),
            suggestKdfSalt = Base64.getDecoder().decode(encryptionParamsArray[1].jsonPrimitive.content),
            kdfIterations = encryptionParamsArray[2].jsonPrimitive.int,
            kdfKeysize = encryptionParamsArray[3].jsonPrimitive.int,
            cipherTagSize = encryptionParamsArray[4].jsonPrimitive.int,
            cipherAlgo = encryptionParamsArray[5].jsonPrimitive.content,
            cipherMode = encryptionParamsArray[6].jsonPrimitive.content,
            compressionType = CompressionType.valueOf(encryptionParamsArray[7].jsonPrimitive.content.uppercase())
        )
        val formatter = TextFormatter.valueOf(jsonArray[1].jsonPrimitive.content.uppercase())
        val openDiscussion = jsonArray[2].jsonPrimitive.int == 1
        val burnAfterReading = jsonArray[3].jsonPrimitive.int == 1

        return PlainPasteAAData(encryptionParams, formatter, openDiscussion, burnAfterReading)
    }
}