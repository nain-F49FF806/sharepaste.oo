package alt.nainapps.sharepaste

import org.junit.Assert
import org.junit.Test
import uniffi.pbcli.Opts
import uniffi.pbcli.PasteFormat


class PbcliLoadedTest {
    @Test
    fun pbcli_load() {
        val res = Opts(url = "https://privatebin.net", format = PasteFormat.PLAINTEXT)
        Assert.assertEquals("https://privatebin.net", res.url)
    }
}