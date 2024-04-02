package alt.nainapps.sharepaste.core

import org.junit.Assert
import org.junit.Test
import uniffi.pbcli.Opts
import uniffi.pbcli.PasteFormat

class PbcliUploadTest {
    @Test
    fun upload_hello() {
        val text = "hello"
        val postPasteResponse = PrivateBinRs().send(text)
        Assert.assertEquals(postPasteResponse.isSuccess(), true)
        println(postPasteResponse.toUrl("https://privatebin.net"))
    }
}