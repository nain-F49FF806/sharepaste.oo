package alt.nainapps.sharepaste.privatebin

import alt.nainapps.sharepaste.privatebin.model.PlainPasteData
import alt.nainapps.sharepaste.privatebin.model.PrivatebinPasteMetaData
import alt.nainapps.sharepaste.privatebin.model.PrivatebinPastePassphrase
import alt.nainapps.sharepaste.privatebin.model.PrivatebinPasteV2
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Base64

class EncryptionTest {
    @Test
    fun test_encrypt_decrypt_roundtrip() {
        val samplePlaintext: String = "Hello!"
        val (base58Seed, privatePasteJson) = swiftEncrypt(samplePlaintext)
        val decryptedPlaintext: String =  swiftDecrypt(privatePasteJson, base58Seed)

        assertEquals(decryptedPlaintext, samplePlaintext)

    }
    @Test
    fun test_known_good_paste_decryption() {
        val samplePlaintext = "a"
        val samplePrivatePasteJson = "{\"status\":0,\"id\":\"619b62853d3d40c6\",\"url\":\"\\/?619b62853d3d40c6\",\"adata\":[[\"tIRyxPTycsKSTTabZ7xNKg==\",\"+id34lhY8hM=\",100000,256,128,\"aes\",\"gcm\",\"zlib\"],\"plaintext\",0,0],\"meta\":{\"created\":1711751675,\"time_to_live\":86388},\"v\":2,\"ct\":\"xGi0ecNOryBsgIp8Na6mVBPM\\/GSFkI8pEmrUxqtNmA==\",\"comments\":[],\"comment_count\":0,\"comment_offset\":0,\"@context\":\"?jsonld=paste\"}"
        val sampleBase58Seed = "ErQ1i8J8o6SNFuhqrhvZATgoZpTf5tJcx6q8gRHPEqDi"

        val decryptedPlaintext = swiftDecrypt(samplePrivatePasteJson, sampleBase58Seed)
        assertEquals(decryptedPlaintext, samplePlaintext)
    }

    @Test
    fun test_known_good_paste_encryption() {
        val samplePlaintext = "a"
        val samplePrivatePasteJson = "{\"status\":0,\"id\":\"619b62853d3d40c6\",\"url\":\"\\/?619b62853d3d40c6\",\"adata\":[[\"tIRyxPTycsKSTTabZ7xNKg==\",\"+id34lhY8hM=\",100000,256,128,\"aes\",\"gcm\",\"zlib\"],\"plaintext\",0,0],\"meta\":{\"created\":1711751675,\"time_to_live\":86388},\"v\":2,\"ct\":\"xGi0ecNOryBsgIp8Na6mVBPM\\/GSFkI8pEmrUxqtNmA==\",\"comments\":[],\"comment_count\":0,\"comment_offset\":0,\"@context\":\"?jsonld=paste\"}"
        val sampleBase58Seed = "ErQ1i8J8o6SNFuhqrhvZATgoZpTf5tJcx6q8gRHPEqDi"


        val plainPaste = PlainPasteData(samplePlaintext)
        val parsedPrivatePaste = PrivatebinPasteV2.fromPrivatebinPasteJson(samplePrivatePasteJson)
        val encryptionParams = parsedPrivatePaste.adata.encryptionParams
        val plainAAData = parsedPrivatePaste.adata
        val passphrase = PrivatebinPastePassphrase(sampleBase58Seed)
        println("sourceSeed: $sampleBase58Seed")
        println("passphraseSeed: ${passphrase.getBase58EncodedSeed()}")
        println("passphrase: $passphrase")
        val cipherBytes = encrypt(plainPaste, plainAAData, passphrase)
        val ct = Base64.getEncoder().encodeToString(cipherBytes)
        assertEquals(ct, parsedPrivatePaste.ct)
        val meta = PrivatebinPasteMetaData(expire = "1day")
        val privatePaste = PrivatebinPasteV2(plainAAData, ct, meta, v = 2)


        val decryptedPlaintext = swiftDecrypt(samplePrivatePasteJson, sampleBase58Seed)
        assertEquals(decryptedPlaintext, samplePlaintext)
    }
}
