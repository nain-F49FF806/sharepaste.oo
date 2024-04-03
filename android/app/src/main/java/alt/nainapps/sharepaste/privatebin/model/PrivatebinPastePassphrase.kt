package alt.nainapps.sharepaste.privatebin.model

import org.bitcoinj.core.Base58
import java.nio.charset.Charset
import java.security.SecureRandom

class PrivatebinPastePassphrase(private val random32BSeed: ByteArray, val final: CharArray) {

    fun getBase58EncodedSeed(): String {
        return Base58.encode(random32BSeed)

    }
    companion object {
        fun derive(password: String? = null): PrivatebinPastePassphrase {
            val random32BSeed = ByteArray(32).also { SecureRandom().nextBytes(it) }
            val passphrase = random32BSeed + (password?.encodeToByteArray() ?: byteArrayOf())
            val charset = Charset.forName("UTF-8")
            return PrivatebinPastePassphrase(
                random32BSeed,
                passphrase.toString(charset).toCharArray()
            )
        }
    }
}