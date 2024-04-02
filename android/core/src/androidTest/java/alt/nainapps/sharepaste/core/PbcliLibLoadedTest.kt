package alt.nainapps.sharepaste.core

import org.junit.Assert
import org.junit.Test
import uniffi.pbcli.Opts
import uniffi.pbcli.PasteFormat


class PbcliLibLoadedTest {
    @Test
    fun addition() {
        val res = Opts(url = "https://privatebin.net", format = PasteFormat.PLAINTEXT)
        Assert.assertEquals("https://privatebin.net", res.url)
    }
}
