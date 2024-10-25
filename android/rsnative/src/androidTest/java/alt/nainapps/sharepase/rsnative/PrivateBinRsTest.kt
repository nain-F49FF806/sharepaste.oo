package alt.nainapps.sharepase.rsnative

import alt.nainapps.sharepaste.rsnative.PrivateBinRs
import org.junit.Assert
import org.junit.Test

class PrivateBinRsTest {
    @Test
    fun upload_hello() {
        val text = "hello"
        val postPasteResponse = PrivateBinRs().send(text)
        Assert.assertEquals(postPasteResponse.isSuccess(), true)
        println(postPasteResponse.toPasteUrl())
    }

    @Test
    fun download_hi() {
        // Test url valid up to 2025-10-25
        val url =
            "https://pb.envs.net/?8222c5b2987f4594#55mDhNz3BkMscWYphyL9ptJS5BN6VDU5acABhADqQroK"
        val decryptedPaste = PrivateBinRs().get(url)
        Assert.assertEquals(decryptedPaste.paste, "hi")
        println(decryptedPaste)
    }

    @Test
    fun roundtrip_hola() {
        val text = "hola"
        val pb = PrivateBinRs()
        val postPasteResponse = pb.send(text)
        Assert.assertEquals(postPasteResponse.isSuccess(), true)

        val pasteUrl = postPasteResponse.toPasteUrl()
        println(pasteUrl)

        val pb2 = PrivateBinRs()
        val decryptedPaste = pb2.get(pasteUrl)
        Assert.assertEquals(decryptedPaste.paste, text)
        println(decryptedPaste)
    }

    @Test
    fun upload_hello_custom_expire() {
        val text = "hello"
        val expire = "1day"
        val pb = PrivateBinRs()
        val opts = pb.getOpts(expire = expire)
        val postPasteResponse = pb.send(text, opts)
        Assert.assertEquals(postPasteResponse.isSuccess(), true)
        println(postPasteResponse.toPasteUrl())
    }
}