package alt.nainapps.sharepaste.rsnative

import uniffi.pbcli.Api
import uniffi.pbcli.DecryptedPaste
import uniffi.pbcli.Opts
import uniffi.pbcli.PasteException
import uniffi.pbcli.PasteFormat
import uniffi.pbcli.PostPasteResponse
import uniffi.pbcli.Url
import java.net.URL

class PrivateBinRs(private val defaultBaseUrl: String? = null) {
    private val defaultOpts = getOpts()

    fun getOpts(
        url: Url? = null,
        format: PasteFormat? = null,
        expire: String? = null,
        burn: Boolean? = null,
        discussion: Boolean? = null
    ): Opts = Opts(
        url = url ?: defaultBaseUrl ?: "https://privatebin.net",
        format = format ?: PasteFormat.PLAINTEXT,
        expire = expire ?: "5min",
        burn = burn ?: false,
        discussion = discussion ?: false
    )

    @Throws(PasteException::class)
    fun send(
        text: String,
        opts: Opts = defaultOpts,
        attachment: String? = null,
        attachmentName: String? = null
    ): PostPasteResponse {
        val decryptedPaste = DecryptedPaste(text, attachment, attachmentName)
        val api = Api(opts.url ?: defaultOpts.url!!, opts)
        return api.postPaste(decryptedPaste, opts.password ?: "", opts)
    }

    @Throws(PasteException::class)
    fun get(url: String): DecryptedPaste {
        val url = URL(url)
        val base = "${url.protocol}://${url.host}" + if (url.port != -1) ":${url.port}" else ""
        val (pasteId, bs58key) = Pair(url.query, url.ref)
        val opts = getOpts(url = url.toString())
        val api = Api(base, opts)
        val paste = api.getPaste(pasteId)
        val decryptedPaste = paste.decrypt(bs58key)
        return decryptedPaste
    }
}
