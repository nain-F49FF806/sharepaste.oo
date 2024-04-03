package alt.nainapps.sharepaste.privatebin.model

import kotlinx.serialization.Serializable

@Serializable
enum class CompressionType {ZLIB, NONE}

@Serializable
enum class TextFormatter {
    PLAINTEXT, SYNTAXHIGHLIGHTING, MARKDOWN
}