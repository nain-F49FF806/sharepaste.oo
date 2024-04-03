package alt.nainapps.sharepaste.privatebin

import alt.nainapps.sharepaste.privatebin.model.CompressionType
import alt.nainapps.sharepaste.privatebin.model.PlainPasteAAData
import alt.nainapps.sharepaste.privatebin.model.PrivatebinPasteEncryptionParams
import alt.nainapps.sharepaste.privatebin.model.TextFormatter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class PlainPasteAADataTest {

    @Test
    fun `test serialization and deserializationn`() {
        // Given
        val original = PlainPasteAAData(
            encryptionParams = PrivatebinPasteEncryptionParams(
                suggestCipherIV = "cipherIv".toByteArray(),
                suggestKdfSalt = "kdfSalt".toByteArray(),
                kdfIterations = 1000,
                kdfKeysize = 256,
                cipherTagSize = 128,
                cipherAlgo = "AES",
                cipherMode = "CBC",
                compressionType = CompressionType.ZLIB
            ),
            formatter = TextFormatter.PLAINTEXT,
            openDiscussion = true,
            burnAfterReading = false
        )

        // When
        println(original)
        val jsonString = Json.encodeToString(original)
        println(jsonString)
        val deserialized = Json.decodeFromString<PlainPasteAAData>(jsonString)

        // Then
        assertEquals(original, deserialized)
    }
}