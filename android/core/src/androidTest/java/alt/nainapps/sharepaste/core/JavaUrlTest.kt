package alt.nainapps.sharepaste.core

import org.junit.Assert
import org.junit.Test
import java.net.URL

class JavaUrlTest {
    @Test
    fun addition() {
        val stringUrl = "https://privatebin.net/?2ea20a8f3551c41b#61T5hUAekhtgdQPm3yw2dwaRBya3rS3g4GP6enEUYeNv"
        val url = URL(stringUrl)
        val base = "${url.protocol}://${url.host}" + if (url.port != -1) ":${url.port}" else ""
        val (pasteId, bs58key) = Pair(url.query, url.ref)
        println("$base, $pasteId, $bs58key")
//        Assert.assertEquals(4, res.value)
//
//        val res2 = calculator.chainAdd(7)
//        Assert.assertEquals(11, res2.value)
    }
}