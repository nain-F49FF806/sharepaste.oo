package alt.nainapps.sharepaste.rsnative

import java.net.URL
import uniffi.pbcli.Api
import uniffi.pbcli.DecryptedPaste
import uniffi.pbcli.Opts
import uniffi.pbcli.PasteFormat
import uniffi.pbcli.PostPasteResponse
import uniffi.pbcli.Url

class PrivateBinRs(private val defaultBaseUrl: String = "https://privatebin.net") {
    private val defaultOpts = getOpts()

    fun getOpts(url: Url? = null, expire: String? = null, burn: Boolean? = null): Opts = Opts(
        url = url ?: defaultBaseUrl,
        format = PasteFormat.PLAINTEXT,
        expire = expire ?: "5min",
        burn = burn ?: false
    )

    fun send(text: String, opts: Opts = defaultOpts): PostPasteResponse {
        val decryptedPaste = DecryptedPaste(text, null, null)
        val api = Api(opts.url ?: defaultBaseUrl, opts)
        val postPasteResponse = api.postPaste(decryptedPaste, opts.password ?: "", opts)
        // postPasteResponse.toUrl(api.base())
        return postPasteResponse
    }

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
