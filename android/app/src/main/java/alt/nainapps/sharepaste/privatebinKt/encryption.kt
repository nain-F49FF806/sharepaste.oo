package alt.nainapps.sharepaste.privatebinKt

import alt.nainapps.sharepaste.privatebinKt.model.CompressionType
import alt.nainapps.sharepaste.privatebinKt.model.PlainPasteAAData
import alt.nainapps.sharepaste.privatebinKt.model.PlainPasteData
import alt.nainapps.sharepaste.privatebinKt.model.PrivatebinPasteEncryptionParams
import alt.nainapps.sharepaste.privatebinKt.model.PrivatebinPasteMetaData
import alt.nainapps.sharepaste.privatebinKt.model.PrivatebinPastePassphrase
import alt.nainapps.sharepaste.privatebinKt.model.PrivatebinPasteV2
import alt.nainapps.sharepaste.privatebinKt.model.TextFormatter
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.DeflaterOutputStream
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


fun deriveKey(passphrase: CharArray, salt: ByteArray, iterations: Int, keySize: Int): SecretKeySpec {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec = PBEKeySpec(passphrase, salt, iterations, keySize)
    val tmp = factory.generateSecret(spec)
    return SecretKeySpec(tmp.encoded, "AES")
}

fun initCipher(key: SecretKeySpec, algo: String, mode: String, tagSize: Int, iv: ByteArray): Cipher {
    val cipher = Cipher.getInstance("$algo/$mode/NoPadding")
    val cipherParams = GCMParameterSpec(tagSize, iv)
    cipher.init(Cipher.ENCRYPT_MODE, key, cipherParams)
    return cipher
}

fun compressBytes(data: ByteArray): ByteArray {
    val outputStream = ByteArrayOutputStream()
    DeflaterOutputStream(outputStream).use { dos ->
        dos.write(data)
    }
    return outputStream.toByteArray()
}

/**
 * Encrypts plaintext using given encryption params and Privatebin v1.3+ key derivation strategy
 *
 * @param pasteData The plaintext to be encrypted, in Privatebin paste_data format.
 * @param pasteAAData The Privatebin paste_meta structure, to be authenticated but not encrypted.
 * @param passphrase  Privatebin passphrase struct, containing random seed and derived final passphrase.
 * @return A byte array containing the encrypted data.
 * */
fun encrypt(
    pasteData: PlainPasteData,
    pasteAAData: PlainPasteAAData,
    passphrase: PrivatebinPastePassphrase
): ByteArray {

    // Initialize encryption params (including kdf_salt, and cipher_iv)
    val encryptionParams = pasteAAData.encryptionParams
    // Generate key
    val kdfKey = deriveKey(
        passphrase.final,
        encryptionParams.kdfSalt,
        encryptionParams.kdfIterations,
        encryptionParams.kdfKeysize
    )
    // Initialise cipher
    val cipher = initCipher(
        kdfKey,
        encryptionParams.cipherAlgo,
        encryptionParams.cipherMode,
        encryptionParams.cipherTagSize,
        encryptionParams.cipherIV
    )
    // Encode pasteData to bytes for processing
    var dataToEncrypt = pasteData.toPasteDataJson().encodeToByteArray()
    // Compress data if specified
    if (encryptionParams.compressionType == CompressionType.ZLIB) {
        dataToEncrypt = compressBytes(dataToEncrypt)
    }
    // Include Authenticated Data in cipher
    cipher.updateAAD(pasteAAData.toPasteMetaJson().encodeToByteArray())
    // Encrypt and return ciphertext
    return cipher.doFinal(dataToEncrypt)
}

fun swiftEncrypt (plaintext: String, password: String? = null): Pair<String, String> {
    val plainPaste = PlainPasteData(plaintext)
    val encryptionParams = PrivatebinPasteEncryptionParams(kdfIterations = 30000)
    val plainAAData = PlainPasteAAData(encryptionParams, TextFormatter.PLAINTEXT, true, true)
    val passphrase = PrivatebinPastePassphrase.derive(password ?: "")
    val cipherBytes = encrypt(plainPaste, plainAAData, passphrase)
    val ct = Base64.getEncoder().encodeToString(cipherBytes)
    val meta = PrivatebinPasteMetaData(expire = "1day")
    val privatePaste = PrivatebinPasteV2(plainAAData, ct, meta, v = 2)
    return  Pair(
        passphrase.getBase58EncodedSeed(),
        privatePaste.toPrivatebinPasteJson()
    )
}