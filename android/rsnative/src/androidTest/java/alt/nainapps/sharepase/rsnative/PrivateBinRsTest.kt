package alt.nainapps.sharepase.rsnative

import org.junit.Assert
import org.junit.Test

class PrivateBinRsTest {
    @Test
    fun upload_hello() {
        val text = "hello"
        val postPasteResponse = PrivateBinRs().send(text)
        Assert.assertEquals(postPasteResponse.isSuccess(), true)
        println(postPasteResponse.toUrl("https://privatebin.net"))
    }

    @Test
    fun download_hi() {
        // Test url valid up to 2024-05-01
        val url = "https://privatebin.net/?2ea20a8f3551c41b#61T5hUAekhtgdQPm3yw2dwaRBya3rS3g4GP6enEUYeNv"
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

        val pasteUrl = postPasteResponse.toUrl(pb.defaultBaseUrl)
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
        println(postPasteResponse.toUrl(pb.defaultBaseUrl))
    }
}