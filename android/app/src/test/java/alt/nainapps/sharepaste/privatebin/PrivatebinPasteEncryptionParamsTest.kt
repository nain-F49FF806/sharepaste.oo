package alt.nainapps.sharepaste.privatebin

import alt.nainapps.sharepaste.privatebin.model.CompressionType
import alt.nainapps.sharepaste.privatebin.model.PrivatebinPasteEncryptionParams
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class PrivatebinPasteEncryptionParamsTest{

    @Test
    fun `test serialization and deserialization`() {
        // Create an instance of PrivatebinPasteEncryptionParams
        val originalParams = PrivatebinPasteEncryptionParams(
            kdfIterations = 100000,
            kdfKeysize = 256,
            cipherTagSize = 128,
            cipherAlgo = "aes",
            cipherMode = "gcm",
            compressionType = CompressionType.ZLIB,
            suggestCipherIV = null,
            suggestKdfSalt = null
        )
        println(originalParams)
        // Serialize the instance using the custom serializer
        val jsonString = Json.encodeToString(originalParams)
        println(jsonString)

        // Deserialize the JSON string back to an instance of PrivatebinPasteEncryptionParams
        val deserializedParams = Json.decodeFromString<PrivatebinPasteEncryptionParams>(jsonString)

        // Check if the original and deserialized instances are equal
        assertEquals(originalParams, deserializedParams)
    }
}
