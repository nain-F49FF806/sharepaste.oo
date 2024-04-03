package alt.nainapps.sharepaste.privatebinKt.model

import kotlinx.serialization.Serializable

@Serializable
enum class CompressionType {ZLIB, NONE}

@Serializable
enum class TextFormatter {
    PLAINTEXT, SYNTAXHIGHLIGHTING, MARKDOWN
}