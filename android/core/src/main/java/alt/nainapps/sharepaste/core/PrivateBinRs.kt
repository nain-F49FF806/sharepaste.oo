package alt.nainapps.sharepaste.core

import uniffi.pbcli.Api
import uniffi.pbcli.DecryptedPaste
import uniffi.pbcli.Opts
import uniffi.pbcli.PasteFormat
import uniffi.pbcli.PostPasteResponseInterface

const val DEMO_INSTANCE_URL = "https://privatebin.net"

class PrivateBinRs(val host: String = DEMO_INSTANCE_URL) {
    private fun getOpts(): Opts {
        return Opts(url = host, format = PasteFormat.PLAINTEXT, expire = "5min")
    }

    fun send(text: String): PostPasteResponseInterface {
        val decryptedPaste = DecryptedPaste(text, null, null)
        val opts = getOpts()
        val api = Api(opts.url ?: host, opts)
        val postPasteResponse = api.postPaste(decryptedPaste, opts.password ?: "", opts)
        // postPasteResponse.toUrl(api.base())
        return postPasteResponse
    }
}
